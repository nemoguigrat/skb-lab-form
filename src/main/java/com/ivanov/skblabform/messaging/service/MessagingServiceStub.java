package com.ivanov.skblabform.messaging.service;

import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.MessageId;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class MessagingServiceStub implements MessagingService {
    @Override
    public <T> MessageId send(Message<T> msg) {
        return msg.getMessageId();
    }

    @Override
    public <T> Message<T> receive(MessageId messageId) throws TimeoutException {
        if (shouldThrowTimeout()) {
            log.error("Should timeout!!!!!");
            sleep();
            throw new TimeoutException("Timeout!");
        }
        if (shouldSleep()) {
            sleep();
        }
        if (shouldReject()) {
            return new Message(VerificationStatus.REJECTED);
        }
        // return our stub message here.
        return new Message(VerificationStatus.VERIFIED);
    }

    @Override
    public <R, A> Message<A> doRequest(Message<R> request) throws TimeoutException {
        final MessageId messageId = send(request);
        return receive(messageId);
    }

    @SneakyThrows
    private static void sleep() {
        Thread.sleep(TimeUnit.MINUTES.toMillis(1));
    }


    private static boolean shouldSleep() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldThrowTimeout() {
        return new Random().nextInt(10) == 1;
    }

    private static boolean shouldReject() {
        return new Random().nextInt(10) == 1;
    }
}