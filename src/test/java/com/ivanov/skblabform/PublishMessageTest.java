package com.ivanov.skblabform;

import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import com.ivanov.skblabform.messaging.event.MessageListenerImpl;
import com.ivanov.skblabform.user.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class PublishMessageTest {
    @Autowired
    private MessageEventPublisher messageEventPublisher;

    @MockBean
    private MessageListenerImpl messageListener;

//    @MockBean
//    private MessageEventPublisher mockedEventPublisher;
//    @MockBean
//    private MessagingServiceStub messagingServiceStub;
//
//    @MockBean
//    private MessageSchedulerService<Object> messageSchedulerService;

    @Test
    @SneakyThrows
    public void test() {
        messageEventPublisher.publishMessagingEvent(new Message<>("test"));
        messageEventPublisher.publishMessagingEvent(new Message<>(10000000L));
        messageEventPublisher.publishMessagingEvent(new Message<>(new UserDto()));
        messageEventPublisher.publishMessagingEvent(new Message<>(new Email("test", "test")));
        Mockito.verify(messageListener, Mockito.times(4)).handleMessage(Mockito.any(Message.class));
    }
}
