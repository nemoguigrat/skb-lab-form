package com.ivanov.skblabform.messaging.event;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class HandledMessage<IN, OUT> implements ResolvableTypeProvider {
    private final IN incomingMessage;
    private final OUT receivedMessage;

    public HandledMessage(IN incomingMessage, OUT receivedMessage) {
        this.incomingMessage = incomingMessage;
        this.receivedMessage = receivedMessage;
    }

    public IN getIncomingMessage() {
        return incomingMessage;
    }

    public OUT getReceivedMessage() {
        return receivedMessage;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(incomingMessage),
                ResolvableType.forInstance(receivedMessage));
    }

    @Override
    public String toString() {
        return "[MessageData " + this.incomingMessage.toString() + " " + this.receivedMessage.toString() + "]";
    }
}
