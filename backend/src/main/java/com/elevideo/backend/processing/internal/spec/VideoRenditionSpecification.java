package com.elevideo.backend.processing.internal.spec;

import com.elevideo.backend.processing.internal.model.BackgroundMode;
import com.elevideo.backend.processing.internal.model.Platform;
import com.elevideo.backend.processing.internal.model.ProcessingMode;
import com.elevideo.backend.processing.internal.model.VideoRendition;
import org.springframework.data.jpa.domain.Specification;

public class VideoRenditionSpecification {

    public static Specification<VideoRendition> belongsToVideo(Long videoId) {
        return (root, query, cb) -> cb.equal(root.get("videoId"), videoId);
    }

    public static Specification<VideoRendition> hasProcessingMode(ProcessingMode mode) {
        return mode == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("processingMode"), mode);
    }

    public static Specification<VideoRendition> hasPlatform(Platform platform) {
        return platform == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("platform"), platform);
    }

    public static Specification<VideoRendition> hasBackgroundMode(BackgroundMode mode) {
        return mode == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("backgroundMode"), mode);
    }
}
