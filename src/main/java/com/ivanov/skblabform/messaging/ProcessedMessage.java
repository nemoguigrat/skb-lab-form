package com.ivanov.skblabform.messaging;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class ProcessedMessage<IN, OUT> implements ResolvableTypeProvider {
    private final IN incomingMessage;
    private final OUT receivedMessage;

    public ProcessedMessage(IN incomingMessage, OUT receivedMessage) {
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
                ResolvableType.forInstance(this.incomingMessage),
                ResolvableType.forInstance(this.receivedMessage));
    }

    @Override
    public String toString() {
        return "[MessageData " + this.incomingMessage.toString() + " " + this.receivedMessage.toString() + "]";
    }
}
