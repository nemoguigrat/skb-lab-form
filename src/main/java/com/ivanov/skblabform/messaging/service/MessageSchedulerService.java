package com.ivanov.skblabform.messaging.service;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@AllArgsConstructor
public class MessageSchedulerService<T> {
    private final MessageEventPublisher messageEventPublisher;
    private final ConcurrentLinkedQueue<Message<T>> notSendingMessages = new ConcurrentLinkedQueue<>();

    public void addMessageInQueue(Message<T> incomingMessage) {
        this.notSendingMessages.add(incomingMessage);
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 2000L)
    public void resendMessages() {
        log.info("trying to resend messages = " + notSendingMessages.size());
        while (!notSendingMessages.isEmpty()) {
            messageEventPublisher.publishMessagingEvent(notSendingMessages.remove());
        }
    }
}
