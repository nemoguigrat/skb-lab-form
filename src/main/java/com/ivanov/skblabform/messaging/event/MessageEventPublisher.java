package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public <T> void publishMessagingEvent(Message<T> message) {
        applicationEventPublisher.publishEvent(message);
    }

    public <IN, OUT> void publishHandledMessage(HandledMessage<IN, OUT> handledMessage) {
        applicationEventPublisher.publishEvent(handledMessage);
    }
}
