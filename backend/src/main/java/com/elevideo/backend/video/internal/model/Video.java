package com.elevideo.backend.video.internal.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String secureUrl;

    @Column(nullable = false)
    private String publicId;

    @Column(nullable = false)
    private String format;

    @Column(nullable = false)
    private Long durationInSeconds;

    private Long    sizeInBytes;
    private Integer width;
    private Integer height;

    /**
     * FK al proyecto. Solo guardamos el ID para no cruzar módulos.
     */
    @Column(name = "project_id", nullable = false, updatable = false)
    private Long projectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VideoStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
