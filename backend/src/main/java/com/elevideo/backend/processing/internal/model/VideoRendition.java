package com.elevideo.backend.processing.internal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "video_rendition", indexes = {
        @Index(name = "idx_video_rendition_video_id", columnList = "video_id")
})
public class VideoRendition {

    @Id
    @GeneratedValue
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String outputUrl;

    @Column
    private String thumbnailUrl;

    @Column
    private String previewUrl;

    @Column
    private Double qualityScore;

    @Column
    private Double durationSeconds;

    @Column
    private Double segmentStart;

    @Column
    private Integer segmentDuration;

    /**
     * FK al video. Solo ID — no @ManyToOne para no cruzar módulos.
     */
    @Column(name = "video_id", nullable = false, updatable = false)
    private Long videoId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessingMode processingMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Quality quality;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BackgroundMode backgroundMode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
