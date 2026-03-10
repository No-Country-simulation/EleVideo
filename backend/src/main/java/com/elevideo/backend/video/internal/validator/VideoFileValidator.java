package com.elevideo.backend.video.internal.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

class VideoFileValidator implements ConstraintValidator<VideoFile, MultipartFile> {

    private static final List<String> ALLOWED_TYPES = List.of(
            "video/mp4",
            "video/quicktime"
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) return true;
        return ALLOWED_TYPES.contains(file.getContentType());
    }
}
