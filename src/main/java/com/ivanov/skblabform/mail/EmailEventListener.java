package com.ivanov.skblabform.mail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class EmailEventListener {
    private final SendMailerStub sendMailerStub;
    private final ConcurrentLinkedQueue<Email> notSendingEmails = new ConcurrentLinkedQueue<>();

    @Async
    @EventListener
    public void handleEmailSending(Email email) {
        log.info("handle message saving");
        try {
            sendMailerStub.sendMail(email.getReceiver(), email.getBody());
        } catch (TimeoutException exception) {
            log.error(exception.getMessage());
            notSendingEmails.add(email);
        }
    }
}
