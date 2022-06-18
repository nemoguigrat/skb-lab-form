package com.ivanov.skblabform.user;

import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.messaging.event.HandledMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserEventListener {
    private final UserService userService;

    @EventListener
    public Email handleUserRegistration(HandledMessage<UserDto, VerificationStatus> handledMessage) {
        log.info("handle user registration");
        VerificationStatus verificationStatus = handledMessage.getOut();
        UserDto userDto = handledMessage.getIn();
        if (verificationStatus == VerificationStatus.VERIFIED) {
            User user = userService.saveUser(userDto);
            log.info("user saved!" + user);
            return new Email(userDto.getEmail(), "Ваша заявка успешно принята!");
        }
        log.error("user verification failed");
        return new Email(userDto.getEmail(), "Заяка на регистрацию отклонена!");
    }
}
