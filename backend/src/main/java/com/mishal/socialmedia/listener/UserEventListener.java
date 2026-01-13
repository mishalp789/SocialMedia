package com.mishal.socialmedia.listener;

import com.mishal.socialmedia.event.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);

    @Async
    @EventListener
    public void handleUserCreated(UserCreatedEvent event){
        log.info("Handling UserCreatedEvent asynchronously for user={}", event.getUsername());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }
}
