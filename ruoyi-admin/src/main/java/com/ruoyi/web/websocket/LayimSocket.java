package com.ruoyi.web.websocket;


import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.layim.domain.Group;
import com.ruoyi.layim.domain.vo.UserChatVo;
import com.ruoyi.layim.enums.ChatType;
import com.ruoyi.layim.enums.OnlineStatus;
import com.ruoyi.layim.service.ChatUserService;
import com.ruoyi.layim.service.GroupService;
import com.ruoyi.layim.service.impl.ChatUserServiceImpl;
import com.ruoyi.layim.utils.LayimUtils;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;

@ServerEndpoint(value = "/websocket/info/{userId}")
@Component
public class LayimSocket {
    private static final Logger LOG = LoggerFactory.getLogger(LayimSocket.class);
    static Map<Long,Session> sessionMap = new HashMap<>();

    static Map<Long,List<UserChatVo>> MessageList = new HashMap<>();

    static Map<Long,Long> PingTimer = new HashMap<>();

    static final Long MAX_TIME_OUT = 60 * 1000L;




// 每个连接的 Session
    @OnOpen
    public void open(Session session,@PathParam("userId") Long userId) throws IOException {
        LOG.info(String.format("初始化成功,初始化的用户id为%s",userId));
        ChatUserService chatUserService = SpringUtil.getBean(ChatUserService.class);
        chatUserService.setStatus(OnlineStatus.online.toString(),userId);
        if(sessionMap.containsKey(userId)){
            session.close();
            LOG.error("此session的用户已存在");
        } else {
            sessionMap.put(userId,session);
        }

        // 查询离线消息
        List<UserChatVo> l = MessageList.get(userId);
        if(l != null){
            for (UserChatVo o:l) {
                session.getBasicRemote().sendText(JSON.toJSONString(o));
            }
            MessageList.remove(userId);
        }

    }

    @OnMessage
    public void receiveMessage(String message, Session session) {
        try {
            JSONObject typeJSON = JSONObject.parseObject(message);
            if(typeJSON.getString("type").equals("ping")){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("type","ping");
                session.getBasicRemote().sendText(JSON.toJSONString(jsonObject));
                PingTimer.put(typeJSON.getLong("userId"),System.currentTimeMillis());
                return;
            }
            JSONObject to = LayimUtils.getToUser(message);
            Long toId = to.getLong("id");
            String type = to.getString("type");
            // 处理群聊消息
            if(ChatType.group.toString().equals(type)){
                sendToGroup(message,toId);
            } else if (ChatType.friend.toString().equals(type)){
                sendToUser(message,toId,session);
            }
        }catch (Exception e){
            LOG.error("获取消息失败",e);
        }

    }

    @OnError
    public void error(Throwable t) {
        print(t.getMessage());
    }

    @OnClose
    public void close(Session session,@PathParam("userId") Long userId) {
        if(ObjectUtils.isEmpty(userId)){
            return;
        }
        LOG.info(String.format("用户离线,用户id为%s",userId));
        Session userSession = sessionMap.get(userId);
        if(userSession == session){
            sessionMap.remove(userId);
            PingTimer.remove(userId);
        }
        ChatUserService chatUserService = SpringUtil.getBean(ChatUserService.class);
        chatUserService.setStatus(OnlineStatus.offline.toString(),userId);
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void sendToUser(String message,Long toId,Session session){
        try{
            Session toSession = sessionMap.get(toId);
            UserChatVo chatMessage = new UserChatVo(message);
            if(ObjectUtils.isEmpty(toSession)){
                // 对方不在线时的逻辑
                JSONObject json = new JSONObject();
                json.put("type","error");
                json.put("msg","对方不在线");
                session.getBasicRemote().sendText(JSON.toJSONString(json));
                //
                List<UserChatVo> messageList = MessageList.get(toId) == null? new ArrayList<>(): MessageList.get(toId);
                messageList.add(chatMessage);
                MessageList.put(toId,messageList);
                return;
            }
            toSession.getBasicRemote().sendText(JSON.toJSONString(chatMessage));
        }catch (Exception e){
            LOG.error("发送消息失败", e);
        }
    }

    private void sendToGroup(String message,Long toId){
        GroupService groupService = SpringUtil.getBean(GroupService.class);
        try{
            List<Group> groups = groupService.queryGroupMember(toId);
            JSONObject mine = LayimUtils.getMine(message);
            Long mineId = mine.getLong("id");
            UserChatVo chatMessage = new UserChatVo(message,"group");
            for(Group o : groups){
                Session toSession = sessionMap.get(o.getUserid());
                if(ObjectUtils.isEmpty(toSession)){
                    // 对方不在线时的逻辑
                    List<UserChatVo> messageList = MessageList.get(o.getUserid()) == null? new ArrayList<>(): MessageList.get(o.getUserid());
                    messageList.add(chatMessage);
                    MessageList.put(o.getUserid(),messageList);
                    continue;
                }
                if(o.getUserid().equals(mineId)){
                    continue;
                }


                toSession.getBasicRemote().sendText(JSON.toJSONString(chatMessage));
            }
        }catch (Exception e){
            LOG.error("发送群聊消息失败",e);
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void checkSession() throws IOException {
        Set<Long> keys = PingTimer.keySet();
        for(Long o : keys){
            Long now = System.currentTimeMillis();
            Long pingTime = PingTimer.get(o);
            if(now - pingTime > MAX_TIME_OUT){
                sessionMap.get(o).close();
                LOG.info("用户" + o + "长时间未收到心跳,关闭此链接");
            }
        }
    }

}
