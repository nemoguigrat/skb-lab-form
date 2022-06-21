package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageListener;
import com.ivanov.skblabform.messaging.service.MessageScheduler;
import com.ivanov.skblabform.messaging.service.MessagingService;
import com.ivanov.skblabform.user.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeoutException;

@ExtendWith(MockitoExtension.class)
public class MessageListenerExceptionTest {
    @InjectMocks
    private MessageListener messageListener;

    @Mock
    private MessagingService messagingService;

    @Mock
    private MessageScheduler messageScheduler;

    @Test
    @SneakyThrows
    public void messageListenerTest_ADD_MESSAGE_IN_QUEUE_WHEN_EXCEPTION() {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class))).thenThrow(new TimeoutException());
        messageListener.handleMessage(new Message<>(new UserDto()));
        Mockito.verify(messageScheduler).addMessageInQueue(Mockito.any(Message.class));
    }
}
