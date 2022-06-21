package com.ivanov.skblabform.unit;

import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.messaging.ProcessedMessage;
import com.ivanov.skblabform.user.UserDto;
import com.ivanov.skblabform.user.UserEventListener;
import com.ivanov.skblabform.user.UserService;
import org.h2.jdbc.JdbcSQLDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserEventListenerTest {
    @InjectMocks
    private UserEventListener userEventListener;

    @Mock
    private UserService userService;

    @Test
    public void userEventListener_VERIFICATION_STATUS_DESCRIPTION() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        User user = new User(userDto);
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenReturn(user);
        Email verified = userEventListener.handleUserRegistration(new ProcessedMessage<>(new UserDto(), VerificationStatus.VERIFIED));
        Email rejected = userEventListener.handleUserRegistration(new ProcessedMessage<>(new UserDto(), VerificationStatus.REJECTED));
        Assertions.assertEquals(verified.getBody(), VerificationStatus.VERIFIED.getDescription());
        Assertions.assertEquals(rejected.getBody(), VerificationStatus.REJECTED.getDescription());
    }

    @Test
    public void userEventListener_THROWS_EXCEPTION() {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@test.com");
        Mockito.when(userService.saveUser(Mockito.any(UserDto.class))).thenThrow(RuntimeException.class);
        Email verified = userEventListener.handleUserRegistration(new ProcessedMessage<>(new UserDto(), VerificationStatus.VERIFIED));
        Assertions.assertEquals(verified.getBody(), VerificationStatus.SAVED.getDescription());
    }
}
