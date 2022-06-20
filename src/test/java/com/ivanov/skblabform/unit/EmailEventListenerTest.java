package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.mail.EmailEventListener;
import com.ivanov.skblabform.mail.service.EmailScheduler;
import com.ivanov.skblabform.mail.service.SendMailerStub;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.concurrent.TimeoutException;

public class EmailEventListenerTest {
    @InjectMocks
    private EmailEventListener emailEventListener;

    @Mock
    private EmailScheduler emailScheduler;
    @Mock
    private SendMailerStub sendMailerStub;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @SneakyThrows
    public void emailEventListener_APPEND_IN_QUEUE_WHEN_THROWS() {
        Mockito.doThrow(TimeoutException.class).when(sendMailerStub).sendMail(Mockito.anyString(), Mockito.anyString());
        emailEventListener.handleEmailSending(new Email("test", "test"));
        emailEventListener.handleEmailSending(new Email("test", "test"));
        Mockito.verify(emailScheduler, Mockito.times(2)).addEmailInQueue(Mockito.any(Email.class));
    }
}
