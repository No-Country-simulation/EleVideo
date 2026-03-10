package com.elevideo.backend.processing.internal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ProcessingMode {
    VERTICAL("vertical"),
    SHORT_AUTO("short_auto"),
    SHORT_MANUAL("short_manual");

    private final String value;

    ProcessingMode(String value) { this.value = value; }

    @JsonValue
    public String getValue() { return value; }

    @JsonCreator
    public static ProcessingMode fromValue(String value) {
        return Arrays.stream(values())
                .filter(m -> m.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ProcessingMode inválido: " + value));
    }
}
