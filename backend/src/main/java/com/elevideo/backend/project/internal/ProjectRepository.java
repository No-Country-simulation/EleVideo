package com.elevideo.backend.project.internal;

import com.elevideo.backend.project.internal.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findByUserId(UUID userId, Pageable pageable);

    Optional<Project> findByIdAndUserId(Long projectId, UUID userId);

    boolean existsByIdAndUserId(Long id, UUID userId);

    @Query("SELECT COUNT(v) FROM Video v WHERE v.projectId = :projectId")
    long countVideosByProjectId(@Param("projectId") Long projectId);
}
