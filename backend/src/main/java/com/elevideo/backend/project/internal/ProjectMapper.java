package com.elevideo.backend.project.internal;

import com.elevideo.backend.project.api.dto.ProjectRequest;
import com.elevideo.backend.project.api.dto.ProjectResponse;
import com.elevideo.backend.project.internal.model.Project;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
interface ProjectMapper {

    Project toEntity(ProjectRequest request);

    ProjectResponse toResponse(Project project, long videoCount);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProjectRequest request, @MappingTarget Project project);
}
