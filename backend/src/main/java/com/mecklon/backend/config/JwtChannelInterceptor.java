package com.mecklon.backend.config;

import com.mecklon.backend.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                String username = jwtService.extractUserName(token.substring(7));
                accessor.setUser(() -> username); // lambda Principal
                accessor.getSessionAttributes().put("username", username);
                System.out.println("User set in CONNECT: " + username);
            }
        } else if (accessor.getUser() == null) {
            // propagate the same Principal to SUBSCRIBE/SEND frames
            // optional: store username in session attributes during CONNECT
            String username = (String) accessor.getSessionAttributes().get("username");
            if (username != null) {
                accessor.setUser(() -> username);
            }
        }

        return message;
    }

}
