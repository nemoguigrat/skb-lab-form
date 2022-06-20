package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import com.ivanov.skblabform.messaging.event.MessageListenerImpl;
import com.ivanov.skblabform.messaging.service.MessageScheduler;
import com.ivanov.skblabform.messaging.service.MessagingService;
import com.ivanov.skblabform.user.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeoutException;

public class MessageListenerExceptionTest {
    @InjectMocks
    private MessageListenerImpl messageListener;

    @Mock
    private MessagingService messagingService;

    @Mock
    private MessageScheduler messageScheduler;

    @Mock
    private MessageEventPublisher messageEventPublisher;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SneakyThrows
    public void messageListenerTest_ADD_MESSAGE_IN_QUEUE_WHEN_EXCEPTION() {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class))).thenThrow(new TimeoutException());
        messageListener.handleMessage(new Message<>(new UserDto()));
        Mockito.verify(messageScheduler).addMessageInQueue(Mockito.any(Message.class));
    }
}
