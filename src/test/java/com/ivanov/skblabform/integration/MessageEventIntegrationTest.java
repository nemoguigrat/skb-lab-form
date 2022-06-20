package com.ivanov.skblabform.integration;

import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import com.ivanov.skblabform.messaging.service.MessagingService;
import com.ivanov.skblabform.user.UserDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class MessageEventIntegrationTest {
    @Autowired
    private MessageEventPublisher messageEventPublisher;

    @MockBean
    private MessagingService messagingService;

    @Test
    @SneakyThrows
    public void messageListenerTest_FOR_STRING() {
        Message<Object> message = new Message<>("test");
        messageEventPublisher.publishMessagingEvent(message);
        //Перехватчик выполняется ассинхронно, а статический verify выполняется в итоге раньше, чем происходит вызов реального метода.
        Mockito.when(messagingService.doRequest(message)).thenReturn(new Message<>(VerificationStatus.VERIFIED));
        Mockito.verify(messagingService, Mockito.timeout(100).times(1)).doRequest(Mockito.any(Message.class));
    }

    @Test
    @SneakyThrows
    public void messageListenerTest_FOR_USER() {
        Message<Object> message = new Message<>(new UserDto());
        messageEventPublisher.publishMessagingEvent(message);
        Mockito.when(messagingService.doRequest(message)).thenReturn(new Message<>(VerificationStatus.VERIFIED));
        Mockito.verify(messagingService, Mockito.timeout(100).times(1)).doRequest(Mockito.any(Message.class));
    }
}
