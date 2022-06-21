package com.ivanov.skblabform.user;

import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.UserRepository;
import com.ivanov.skblabform.exception.UserExistsException;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.event.MessageEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageEventPublisher messageEventPublisher;

    public void verificationUser(UserDto userDto) {
        // в случае, если это новый юзер, то все нормально и мы говорим, что его заяка обрабатывается,
        // если такой пользователь уже в системе, то выскочит ошибка, которую перехватит обработчик и скажет, что пользователь уже существует
        if (userRepository.findFirstUserByEmailOrLogin(userDto.getEmail(), userDto.getLogin()).isPresent()) {
            throw new UserExistsException("User exists!");
        }
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        //публикуется ивент, вся последующая цепочка выполняется ассинхронно.
        messageEventPublisher.publishMessagingEvent(new Message<>(userDto));
    }

    public User saveUser(UserDto userDto) {
        return userRepository.save(new User(userDto));
    }
}
