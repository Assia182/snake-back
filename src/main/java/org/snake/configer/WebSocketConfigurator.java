package org.snake.configer;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfigurator implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/comment/new", "/comment/deleteById",
                "/snake/changeDofP", "/snake/fodderOfSnake", "/snake/deleted", "/snake/chat", "/snake/newHighScore");
        //config.enableSimpleBroker("/comment/deleteById");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*");//.withSockJS();
    }
}

