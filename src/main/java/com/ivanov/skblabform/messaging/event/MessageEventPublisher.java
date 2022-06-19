package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.ProcessedMessage;
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

    public <IN, OUT> void publishProcessedMessage(ProcessedMessage<IN, OUT> processedMessage) {
        applicationEventPublisher.publishEvent(processedMessage);
    }
}
