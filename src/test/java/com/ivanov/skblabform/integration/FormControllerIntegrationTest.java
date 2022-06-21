package com.ivanov.skblabform.integration;

import com.ivanov.skblabform.controller.FormController;
import com.ivanov.skblabform.dao.User;
import com.ivanov.skblabform.dao.UserRepository;
import com.ivanov.skblabform.dao.VerificationStatus;
import com.ivanov.skblabform.mail.service.SendMailerStub;
import com.ivanov.skblabform.messaging.Message;
import com.ivanov.skblabform.messaging.service.MessagingService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class FormControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FormController formController;

    @MockBean
    private MessagingService messagingService;

    @MockBean
    private SendMailerStub sendMailerStub;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void formControllerTest_WHEN_NO_EXCEPTIONS() throws Exception {
        Mockito.when(messagingService.doRequest(Mockito.any(Message.class))).thenReturn(new Message(VerificationStatus.VERIFIED));
        Mockito.doNothing().when(sendMailerStub).sendMail(Mockito.anyString(), Mockito.anyString());
        Mockito.when(userRepository.findFirstUserByEmailOrLogin(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("email", "email")
                .param("login", "login")
                .param("name", "name")
                .param("password", "Abcd4Abcd4"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeDoesNotExist("exceptions"))
                .andExpect(model().attribute("message", VerificationStatus.PROCESSING.getDescription()))
                .andDo(MockMvcResultHandlers.print());
        Mockito.verify(messagingService).doRequest(Mockito.any(Message.class));
        Mockito.verify(sendMailerStub, Mockito.timeout(200).times(1)).sendMail(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void formControllerTest_WHEN_USER_EXISTS() throws Exception {
        Mockito.when(userRepository.findFirstUserByEmailOrLogin(Mockito.anyString(), Mockito.anyString())).thenReturn(Optional.of(new User()));
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "email")
                        .param("login", "login")
                        .param("name", "name")
                        .param("password", "Abcd4Abcd4"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(model().attribute("exceptions", Matchers.hasItem("User exists!"))).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void formControllerTest_WHEN_VALIDATION_FAILED() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "")
                        .param("login", "")
                        .param("name", "")
                        .param("password", "Abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(model().attribute("exceptions", Matchers.iterableWithSize(4))).andDo(MockMvcResultHandlers.print());
    }
}
