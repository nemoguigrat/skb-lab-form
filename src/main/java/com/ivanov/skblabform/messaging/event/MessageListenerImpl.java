package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.MessagingService;
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
    private final ConcurrentLinkedQueue<Message<T>> notSendingMessages = new ConcurrentLinkedQueue<>();

    @Override
    @EventListener
    @Async
    public <A> void handleMessage(Message<T> incomingMessage) {
        log.error("Событие полученно   " + incomingMessage.getMessage().toString());
        try {
            // будем предполагать, что внешняя шина умеет только валидировать
            // данные и возращает статус в случае когда мы отпраляем объект пользователя
            Message<A> receivedMessage = messagingService.doRequest(incomingMessage);
            log.error("Запрос выполнен " + receivedMessage.getMessage().toString());
            messageEventPublisher.publishHandledMessage(
                    new HandledMessage<>(incomingMessage.getMessage(), receivedMessage.getMessage()));
        } catch (TimeoutException exception) {
            log.error(exception.getMessage());
            notSendingMessages.add(incomingMessage);
        }
    }
}
