package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.ProcessedMessage;
import com.ivanov.skblabform.messaging.service.MessageScheduler;
import com.ivanov.skblabform.messaging.service.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Slf4j
@AllArgsConstructor
@Service
public class MessageListener {
    private final MessageEventPublisher messageEventPublisher;
    private final MessagingService messagingService;
    private final MessageScheduler messageScheduler;

    @Async
    @EventListener
    public void handleMessage(Message<?> incomingMessage) {
        try {
            log.info("handling message");
            Message<?> receivedMessage = messagingService.doRequest(incomingMessage);
            messageEventPublisher.publishProcessedMessage(
                    new ProcessedMessage<>(incomingMessage.getMessage(), receivedMessage.getMessage()));
        } catch (TimeoutException exception) {
            log.error(exception.getMessage());
            messageScheduler.addMessageInQueue(incomingMessage);
        }
    }
}
