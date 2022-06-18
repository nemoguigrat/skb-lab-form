package com.ivanov.skblabform.dao;

import java.io.Serializable;

public enum VerificationStatus implements Serializable {
    SAVED("Данные уже в системе. Отказ."),
    VERIFIED("Верификация успешна! Данные сохранены."),
    REJECTED("Верификация не пройдена. Можете попробовать снова.");

    private final String description;

    VerificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
