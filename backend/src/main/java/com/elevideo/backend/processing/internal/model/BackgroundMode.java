package com.elevideo.backend.processing.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum BackgroundMode {
    SMART_CROP("smart_crop"),
    BLURRED("blurred"),
    SOLID_COLOR("black");

    private final String value;

    BackgroundMode(String value) { this.value = value; }

    @JsonValue
    public String getValue() { return value; }

    @JsonCreator
    public static BackgroundMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(b -> b.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("BackgroundMode inválido: " + value));
    }
}
