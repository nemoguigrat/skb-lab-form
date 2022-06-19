package com.ivanov.skblabform.user;

import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.Email;
import com.ivanov.skblabform.messaging.ProcessedMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class UserEventListener {
    private final UserService userService;

    @EventListener
    public Email handleUserRegistration(ProcessedMessage<UserDto, VerificationStatus> processedMessage) {
        log.info("handle user registration");
        VerificationStatus verificationStatus = processedMessage.getReceivedMessage();
        UserDto userDto = processedMessage.getIncomingMessage();
        if (verificationStatus.equals(VerificationStatus.VERIFIED)) {
            try {
                User user = userService.saveUser(userDto);
                log.info("user saved!" + user);
            } catch (Exception exception) {
                log.error(exception.getMessage());
                return new Email(userDto.getEmail(), VerificationStatus.SAVED.getDescription());
            }
        }
        return new Email(userDto.getEmail(), verificationStatus.getDescription());
    }
}
