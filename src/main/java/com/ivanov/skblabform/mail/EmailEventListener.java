package com.ivanov.skblabform.mail;

import javassist.bytecode.analysis.Executor;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@Service
@AllArgsConstructor
@Slf4j
public class EmailEventListener {
    private final ExecutorService executorService = Executors.newWorkStealingPool();
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

    @SneakyThrows
    @Scheduled(fixedDelay = 3000L)
    public void resendEmails() {
        log.info("trying to resend emails size = " + notSendingEmails.size());
        Deque<Runnable> tasks = new LinkedList<>();
        while (!notSendingEmails.isEmpty()) {
            tasks.add(() -> handleEmailSending(notSendingEmails.remove()));
        }
        tasks.forEach(executorService::submit);
    }
}
