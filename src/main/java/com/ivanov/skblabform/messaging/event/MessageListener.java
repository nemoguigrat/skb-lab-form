package com.ivanov.skblabform.messaging.event;

import com.ivanov.skblabform.messaging.Message;

public interface MessageListener<T> {
    <A> void handleMessage(Message<T> incomingMessage);
}
