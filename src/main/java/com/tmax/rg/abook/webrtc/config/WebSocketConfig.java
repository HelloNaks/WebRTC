package com.tmax.rg.abook.webrtc.config;


import com.tmax.rg.abook.webrtc.RoomManager;
import com.tmax.rg.abook.webrtc.UserRegistry;
import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public UserRegistry registry() { return new UserRegistry();}

    @Bean
    public RoomManager roomManager() { return new RoomManager();}

    @Bean
    public WhiteboardHandler whiteboardHandler() {return new WhiteboardHandler();}

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

    @Bean
    public ServletServerContainerFactoryBean createServletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        return container;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(whiteboardHandler(), "/whiteboard");
    }
}
