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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value = "/websocket/info/{userId}")
@Component
public class LayimSocket {
    private static final Logger LOG = LoggerFactory.getLogger(LayimSocket.class);
    static Map<Long,Session> sessionMap = new HashMap<>();


// 每个连接的 Session
    @OnOpen
    public void open(Session session,@PathParam("userId") Long userId) throws IOException {
        LOG.info(String.format("初始化成功,初始化的用户id为%s",userId));
        ChatUserService chatUserService = SpringUtil.getBean(ChatUserService.class);
        chatUserService.setStatus(OnlineStatus.online.toString(),userId);
        sessionMap.put(userId,session);
    }

    @OnMessage
    public void receiveMessage(String message, Session session) {
        JSONObject to = LayimUtils.getToUser(message);
        Long toId = to.getLong("id");
        String type = to.getString("type");
        // 处理群聊消息
        if(ChatType.group.toString().equals(type)){
            sendToGroup(message,toId);
        } else if (ChatType.friend.toString().equals(type)){
            sendToUser(message,toId);
        }
    }

    @OnError
    public void error(Throwable t) {
        print(t.getMessage());
    }

    @OnClose
    public void close(Session session,@PathParam("userId") Long userId) {
        LOG.info(String.format("用户离线,用户id为%s",userId));
        sessionMap.remove(userId);
        ChatUserService chatUserService = SpringUtil.getBean(ChatUserService.class);
        chatUserService.setStatus(OnlineStatus.offline.toString(),userId);
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void sendToUser(String message,Long toId){
        try{
            Session toSession = sessionMap.get(toId);
            if(ObjectUtils.isEmpty(toSession)){
                // 对方不在线时的逻辑
            }
            UserChatVo chatMessage = new UserChatVo(message);
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
            for(Group o : groups){
                Session toSession = sessionMap.get(o.getUserid());
                if(ObjectUtils.isEmpty(toSession)){
                    // 对方不在线时的逻辑
                }
                if(o.getUserid().equals(mineId)){
                    continue;
                }
                UserChatVo chatMessage = new UserChatVo(message,"group");
                toSession.getBasicRemote().sendText(JSON.toJSONString(chatMessage));
            }
        }catch (Exception e){
            LOG.error("发送群聊消息失败",e);
        }
    }

}
