package com.elevideo.backend.processing.internal.model;

import java.util.Arrays;
import java.util.Set;

public enum JobLifecycleGroup {

    ACTIVE(Set.of(JobStatus.PENDING, JobStatus.PROCESSING)),
    FINISHED(Set.of(JobStatus.COMPLETED, JobStatus.FAILED, JobStatus.CANCELLED));

    private final Set<JobStatus> statuses;

    JobLifecycleGroup(Set<JobStatus> statuses) { this.statuses = statuses; }

    public Set<JobStatus> getStatuses() { return statuses; }

    public static JobLifecycleGroup fromStatus(JobStatus status) {
        return Arrays.stream(values())
                .filter(g -> g.statuses.contains(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No lifecycle group para: " + status));
    }
}
