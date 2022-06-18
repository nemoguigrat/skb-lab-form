package com.ivanov.skblabform.messaging.event;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public class HandledMessage<IN, OUT> implements ResolvableTypeProvider {
    private IN in;
    private OUT out;

    public HandledMessage(IN in, OUT out) {
        this.in = in;
        this.out = out;
    }

    public IN getIn() {
        return in;
    }

    public OUT getOut() {
        return out;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(),
                ResolvableType.forInstance(in),
                ResolvableType.forInstance(out));
    }

    @Override
    public String toString() {
        return "[MessageData " + this.in.toString() + " " + this.out.toString() + "]";
    }
}
