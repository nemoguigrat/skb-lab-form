package com.ivanov.skblabform.messaging;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MessageId {
    private final UUID messageId;

    public MessageId(UUID messageId) {
        this.messageId = messageId;
    }
}
