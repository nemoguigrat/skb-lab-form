package com.ivanov.skblabform.dao;

import java.io.Serializable;

public enum VerificationStatus implements Serializable {
    SAVED("Данные уже в системе. Отказ."),
    VERIFIED("Верификация успешна! Данные сохранены."),
    REJECTED("Верификация не пройдена. Можете попробовать снова."),
    PROCESSING("Ваша заявка принята, идет верификация.");

    private final String description;

    VerificationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
