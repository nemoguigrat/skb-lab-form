package com.ivanov.skblabform.messaging.service;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@AllArgsConstructor
@Service
public class MessageScheduler {
    private final MessageEventPublisher messageEventPublisher;
    private final ConcurrentLinkedQueue<Message<?>> notSendingMessages = new ConcurrentLinkedQueue<>();

    public void addMessageInQueue(Message<?> incomingMessage) {
        this.notSendingMessages.add(incomingMessage);
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 3000L)
    public void resendMessages() {
        // джобы в отдельных сервисах, это упрощает тестирование.
        log.info("trying to resend messages = " + notSendingMessages.size());
        while (!notSendingMessages.isEmpty()) {
            // просто переопубликует ивент с сообщением, так цепочка вызовов сохраняется и все обрабатывается
            messageEventPublisher.publishMessagingEvent(notSendingMessages.remove());
        }
    }
}
