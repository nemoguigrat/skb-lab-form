package com.ivanov.skblabform.mail.service;

import java.util.concurrent.TimeoutException;

public interface SendMailer {
    void sendMail (String receiver, String message) throws TimeoutException;
}
