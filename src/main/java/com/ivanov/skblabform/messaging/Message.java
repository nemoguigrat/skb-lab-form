package com.ivanov.skblabform.messaging;

import java.util.UUID;

public class Message<T> {
    private final MessageId messageId;
    private final T message;

    public Message(T payload) {
        this.messageId = new MessageId(UUID.randomUUID());
        this.message = payload;
    }

    public MessageId getMessageId() {
        return messageId;
    }

    public T getMessage() {
        return message;
    }
}
