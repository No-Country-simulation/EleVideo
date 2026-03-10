package com.elevideo.backend.video.internal;

import com.elevideo.backend.video.internal.model.Video;
import com.elevideo.backend.video.internal.model.VideoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("""
        SELECT v FROM Video v
        WHERE v.projectId = :projectId
        AND (:searchTerm IS NULL OR v.title ILIKE :searchTerm)
        AND (:status    IS NULL OR v.status = :status)
    """)
    Page<Video> findProjectVideos(
            @Param("projectId")  Long        projectId,
            @Param("searchTerm") String      searchTerm,
            @Param("status")     VideoStatus status,
            Pageable             pageable
    );

    boolean existsByIdAndProjectId(Long videoId, Long projectId);
}
