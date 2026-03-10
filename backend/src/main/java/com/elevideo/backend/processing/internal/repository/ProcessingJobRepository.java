package com.elevideo.backend.processing.internal.repository;

import com.elevideo.backend.processing.internal.model.ProcessingJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProcessingJobRepository
        extends JpaRepository<ProcessingJob, Long>, JpaSpecificationExecutor<ProcessingJob> {

    Optional<ProcessingJob> findByJobId(String jobId);

    @Query("""
        SELECT p FROM ProcessingJob p
        WHERE p.jobId   = :jobId
          AND p.videoId = :videoId
    """)
    Optional<ProcessingJob> findByJobIdAndVideoId(
            @Param("jobId")   String jobId,
            @Param("videoId") Long   videoId
    );

    Optional<ProcessingJob> findByVideoRenditionId(Long renditionId);

    boolean existsByVideoId(Long videoId);
}
