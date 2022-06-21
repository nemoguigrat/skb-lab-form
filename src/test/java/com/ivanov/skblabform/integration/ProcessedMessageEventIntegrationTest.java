package com.ivanov.skblabform.integration;

import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.ProcessedMessage;
import com.ivanov.skblabform.messaging.event.MessageListener;
import com.ivanov.skblabform.messaging.service.MessagingService;
import com.ivanov.skblabform.user.UserDto;
import com.ivanov.skblabform.user.UserEventListener;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.PayloadApplicationEvent;

@SpringBootTest
public class ProcessedMessageEventIntegrationTest {
    @Autowired
    private MessageListener<Object> messageListener;

    @MockBean
    private UserEventListener userEventListener;

    @MockBean
    private MessagingService messagingService;

    @Captor
    private ArgumentCaptor<ProcessedMessage<UserDto, VerificationStatus>> eventArgumentCaptor;

    @Test
    @SneakyThrows
    public void userEventListener_CALLED_WHEN_USERDTO() {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class)))
                .thenReturn(new Message<>(VerificationStatus.VERIFIED));
        UserDto userDto = new UserDto();
        userDto.setName("test name");
        userDto.setEmail("email@email.com");
        messageListener.handleMessage(new Message<>(userDto));
        //TODO проверить с помощью Thread.sleep()
        Mockito.verify(userEventListener, Mockito.timeout(500).times(1))
                .handleUserRegistration(eventArgumentCaptor.capture());
        Assertions.assertEquals("email@email.com",
                eventArgumentCaptor.getValue().getIncomingMessage().getEmail());
    }

    @Test
    @SneakyThrows
    public void userEventListener_NOT_CALLED_WHEN_STRING() {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class)))
                .thenReturn(new Message<>(VerificationStatus.VERIFIED));
        Message<Object> message = new Message<>("test string");
        messageListener.handleMessage(message);
        Mockito.verify(userEventListener, Mockito.timeout(500).times(0))
                .handleUserRegistration(Mockito.any(ProcessedMessage.class));
    }

    @Test
    @SneakyThrows
    public void userEventListener_NOT_CALLED_WHEN_NOT_STATUS() {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class)))
                .thenReturn(new Message<>("some string"));
        UserDto userDto = new UserDto();
        userDto.setName("test name");
        userDto.setEmail("email@email.com");
        messageListener.handleMessage(new Message<>(userDto));
        Mockito.verify(userEventListener, Mockito.timeout(500).times(0))
                .handleUserRegistration(Mockito.any(ProcessedMessage.class));
    }
}
