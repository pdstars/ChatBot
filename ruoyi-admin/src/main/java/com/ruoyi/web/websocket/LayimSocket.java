package com.ruoyi.web.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.layim.domain.vo.UserChatVo;
import com.ruoyi.layim.service.impl.ChatUserServiceImpl;
import com.ruoyi.layim.utils.LayimUtils;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        sessionMap.put(userId,session);
    }

    @OnMessage
    public void receiveMessage(String message, Session session) {
        JSONObject to = LayimUtils.getToUser(message);
        Long toId = to.getLong("id");
        Session toSession = sessionMap.get(toId);
        if(ObjectUtils.isEmpty(toSession)){
            // 对方不在线时的逻辑
        }
        try{
            UserChatVo chatMessage = new UserChatVo(message);
            toSession.getBasicRemote().sendText(JSON.toJSONString(chatMessage));
        }catch (Exception e){
            LOG.error("发送消息失败", e);
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
    }

    private void print(String msg) {
        System.out.println(msg);
    }

}
