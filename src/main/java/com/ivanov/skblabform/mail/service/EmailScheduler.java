package com.ivanov.skblabform.mail.service;

import com.ivanov.skblabform.mail.Email;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
@AllArgsConstructor
public class EmailScheduler {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ConcurrentLinkedQueue<Email> notSendingEmails = new ConcurrentLinkedQueue<>();

    public void addEmailInQueue(Email incomingMessage) {
        this.notSendingEmails.add(incomingMessage);
    }

    @SneakyThrows
    @Scheduled(fixedDelay = 3000L)
    public void resendEmails() {
        log.info("trying to resend messages = " + notSendingEmails.size());
        while (!notSendingEmails.isEmpty()) {
            applicationEventPublisher.publishEvent(notSendingEmails.remove());
        }
    }
}
