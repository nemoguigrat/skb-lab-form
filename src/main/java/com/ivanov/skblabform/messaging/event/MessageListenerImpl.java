package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.service.MessageSchedulerService;
import com.ivanov.skblabform.messaging.service.MessagingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@AllArgsConstructor
public class MessageListenerImpl<T> implements MessageListener<T> {
    private final MessageEventPublisher messageEventPublisher;
    private final MessagingService messagingService;
    private final MessageSchedulerService<T> messageSchedulerService;

    @Override
    @EventListener
    @Async
    public <A> void handleMessage(Message<T> incomingMessage) {
        log.info(Thread.currentThread().toString());
        log.info("Событие полученно   " + incomingMessage.getMessage().toString());
        try {
            Message<A> receivedMessage = messagingService.doRequest(incomingMessage);
            log.info("Запрос выполнен " + receivedMessage.getMessage().toString());
            messageEventPublisher.publishHandledMessage(
                    new HandledMessage<>(incomingMessage.getMessage(), receivedMessage.getMessage()));
        } catch (TimeoutException exception) {
            log.error(exception.getMessage());
            messageSchedulerService.addMessageInQueue(incomingMessage);
        }
    }
}
