package com.elevideo.backend.processing.internal.repository;

import com.elevideo.backend.processing.internal.model.VideoRendition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRenditionRepository
        extends JpaRepository<VideoRendition, Long>, JpaSpecificationExecutor<VideoRendition> {

    Optional<VideoRendition> findByIdAndVideoId(Long renditionId, Long videoId);
}
