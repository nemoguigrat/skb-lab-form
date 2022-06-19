package com.ivanov.skblabform.messaging;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

import java.util.UUID;

public class Message<T> implements ResolvableTypeProvider {
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

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(this.message));
    }
}
