package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.mail.service.EmailScheduler;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import com.ivanov.skblabform.messaging.service.MessageScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class SchedulingServicesTest {
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EmailScheduler emailScheduler;

    @Mock
    private MessageEventPublisher messageEventPublisher;

    @InjectMocks
    private MessageScheduler messageScheduler;

    @Test
    public void test() {
        Email email = new Email("test", "test");
        for (int i = 0; i < 15; i++) {
            emailScheduler.addEmailInQueue(email);
        }
        emailScheduler.resendEmails();
        Mockito.verify(applicationEventPublisher, Mockito.times(15)).publishEvent(Mockito.any(Email.class));
    }

    @Test
    public void test1() {
        Message<Object> message = new Message<>("test");
        for (int i = 0; i < 15; i++) {
            messageScheduler.addMessageInQueue(message);
        }
        messageScheduler.resendMessages();
        Mockito.verify(messageEventPublisher, Mockito.times(15)).publishMessagingEvent(Mockito.any(Message.class));
    }
}
