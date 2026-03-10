package com.elevideo.backend.processing.internal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "processing_jobs", indexes = {
        @Index(name = "idx_processing_jobs_job_id",       columnList = "job_id"),
        @Index(name = "idx_processing_jobs_video_status", columnList = "video_id, status")
})
public class ProcessingJob {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "job_id", nullable = false, unique = true, updatable = false)
    private String jobId;

    /**
     * FK al video. Solo ID — no @ManyToOne para no cruzar módulos.
     */
    @Column(name = "video_id", nullable = false, updatable = false)
    private Long videoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;

    @Column
    private Integer progressPercent;

    @Column
    private String phase;

    @Column
    private Double elapsedSeconds;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingMode processingMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Quality quality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BackgroundMode backgroundMode;

    private Integer etaSeconds;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "video_rendition_id")
    private VideoRendition videoRendition;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
