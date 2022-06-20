package com.ivanov.skblabform.integration;

import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.mail.EmailEventListener;
import com.ivanov.skblabform.messaging.ProcessedMessage;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import com.ivanov.skblabform.user.UserDto;
import com.ivanov.skblabform.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class EmailEventIntegrationTest {
    @Autowired
    private MessageEventPublisher messageEventPublisher;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailEventListener emailEventListener;

    @Test
    public void emailEvent_VERIFYSTATUS_NOT_SAVED() {
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenReturn(new User());
        messageEventPublisher.publishProcessedMessage(new ProcessedMessage<>(new UserDto(), VerificationStatus.VERIFIED));
        messageEventPublisher.publishProcessedMessage(new ProcessedMessage<>(new UserDto(), VerificationStatus.REJECTED));
        Mockito.verify(emailEventListener, Mockito.times(2)).handleEmailSending(Mockito.any(Email.class));
    }

    @Test
    public void emailEvent_VERIFYSTATUS_SAVED() {
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenThrow(RuntimeException.class);
        messageEventPublisher.publishProcessedMessage(new ProcessedMessage<>(new UserDto(), VerificationStatus.REJECTED));
        Mockito.verify(emailEventListener, Mockito.times(1)).handleEmailSending(Mockito.any(Email.class));
    }
}
