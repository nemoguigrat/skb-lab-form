package com.ivanov.skblabform.mail;

import com.ivanov.skblabform.mail.service.EmailScheduler;
import com.ivanov.skblabform.mail.service.SendMailerStub;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class EmailEventListener {
    private final SendMailerStub sendMailerStub;

    private final EmailScheduler emailScheduler;
    @Async
    @EventListener
    public void handleEmailSending(Email email) {
        log.info("handle email sending");
        try {
            sendMailerStub.sendMail(email.getReceiver(), email.getBody());
        } catch (TimeoutException exception) {
            log.error(exception.getMessage());
            emailScheduler.addEmailInQueue(email);
        }
    }
}
