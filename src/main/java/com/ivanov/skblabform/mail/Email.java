package com.ivanov.skblabform.mail;

public class Email {
    private final String receiver;
    private final String body;

    public Email(String receiver, String body) {
        this.receiver = receiver;
        this.body = body;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getBody() {
        return body;
    }
}
