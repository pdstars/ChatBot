package com.ruoyi.web.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {
    /**
     * ServerEndpointExporter 将会扫描所有使用 @ServerEndpoint 注解标记的类，并将它们注册为 WebSocket 服务端点
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

    }




}
