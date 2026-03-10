package com.elevideo.backend.processing.internal.spec;

import com.elevideo.backend.processing.internal.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class ProcessingJobSpecification {

    public static Specification<ProcessingJob> belongsToVideo(Long videoId) {
        return (root, query, cb) -> cb.equal(root.get("videoId"), videoId);
    }

    public static Specification<ProcessingJob> hasStatusIn(Set<JobStatus> statuses) {
        return (statuses == null || statuses.isEmpty())
                ? null
                : (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<ProcessingJob> hasProcessingMode(ProcessingMode mode) {
        return mode == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("processingMode"), mode);
    }

    public static Specification<ProcessingJob> hasPlatform(Platform platform) {
        return platform == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("platform"), platform);
    }

    public static Specification<ProcessingJob> hasBackgroundMode(BackgroundMode mode) {
        return mode == null
                ? null
                : (root, query, cb) -> cb.equal(root.get("backgroundMode"), mode);
    }

    public static Specification<ProcessingJob> hasLifecycleGroup(JobLifecycleGroup group) {
        return group == null
                ? null
                : (root, query, cb) -> root.get("status").in(group.getStatuses());
    }
}
