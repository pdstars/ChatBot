package com.ruoyi.web.websocket;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@ServerEndpoint(value = "/websocket/info", configurator = SocketConfigurator.class)
@Component
public class LayimSocket {

//    private IUserLogService userLogService = (IUserLogService) WebChatFactory.beanFactory("userLogService");
//
//    private IMessage messageService = (IMessage) WebChatFactory.beanFactory("message");

    @OnOpen
    public void open(Session session) {
//        SocketUser user = new SocketUser();
//        user.setSession(session);
//        user.setUserId(uid);

        // 保存在线列表
//        WebChatFactory.createManager().addUser(user);
//        userLogService.insertLog(user, UserLogType.LOGIN);
//        print("当前在线用户：" + WebChatFactory.createManager().getOnlineCount());
//        print("缓存中的用户个数：" + new OnLineUserManager().getOnLineUsers().size());
//        //通知所有人
//        String message = MessageParse.ServiceOnlineStatus(uid, OnlineStatus.ONLINE);
//        WebChatFactory.createManager().notifyOthers(user, message);
        System.out.println("初始化成功");
    }

    @OnMessage
    public void receiveMessage(String message, Session session) {
        System.out.println("收到消息" + message);
    }

    @OnError
    public void error(Throwable t) {
        print(t.getMessage());
    }

    @OnClose
    public void close(Session session) {

//        SocketUser user = new SocketUser();
//        user.setSession(session);
//        user.setUserId(0);
//        // 移除该用户
//        int uid = WebChatFactory.createManager().removeUser(user);
//        user.setUserId(uid);
//        userLogService.insertLog(user, UserLogType.LOGOUT);
        System.out.println("用户掉线" );
        // print("当前在线用户：" + WebChatFactory.createManager().getOnlineCount());
        // print("缓存中的用户个数：" + new OnLineUserManager().getOnLineUsers().size());
        //通知所有人
//        String message = MessageParse.ServiceOnlineStatus(uid, OnlineStatus.OFFLINE);
//        WebChatFactory.createManager().notifyOthers(user, message);
    }

    private void print(String msg) {
        System.out.println(msg);
    }

}
