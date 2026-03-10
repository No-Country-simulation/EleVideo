package com.elevideo.backend.video.internal.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VideoFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface VideoFile {
    String message() default "Formato no soportado. Usa MP4 o MOV.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
