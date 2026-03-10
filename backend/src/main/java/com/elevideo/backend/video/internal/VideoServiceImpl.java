package com.elevideo.backend.video.internal;

import com.elevideo.backend.project.api.ProjectService;
import com.elevideo.backend.shared.event.DomainEventPublisher;
import com.elevideo.backend.shared.security.CurrentUserProvider;
import com.elevideo.backend.video.api.VideoService;
import com.elevideo.backend.video.api.dto.CreateVideoRequest;
import com.elevideo.backend.video.api.dto.VideoResponse;
import com.elevideo.backend.video.api.dto.VideoSearchRequest;
import com.elevideo.backend.video.internal.event.VideoUploadedEvent;
import com.elevideo.backend.video.internal.model.Video;
import com.elevideo.backend.video.internal.storage.CloudinaryUploadResponse;
import com.elevideo.backend.video.internal.storage.VideoStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class VideoServiceImpl implements VideoService {

    private final VideoRepository      videoRepository;
    private final VideoMapper          videoMapper;
    private final VideoStoragePort     storagePort;
    private final ProjectService       projectService;
    private final CurrentUserProvider  currentUserProvider;
    private final DomainEventPublisher eventPublisher;

    @Override
    @Transactional
    public VideoResponse createVideo(Long projectId, CreateVideoRequest request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        projectService.assertProjectOwnedByUser(projectId, userId);

        CloudinaryUploadResponse uploadRes = storagePort.upload(request.video());

        try {
            Video video = videoMapper.toVideo(request.title(), uploadRes);
            video.setProjectId(projectId);
            videoRepository.save(video);

            eventPublisher.publish(new VideoUploadedEvent(
                    video.getId(), projectId, video.getTitle(), video.getPublicId()
            ));

            return videoMapper.toVideoResponse(video);

        } catch (Exception ex) {
            storagePort.delete(uploadRes.publicId());
            throw ex;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VideoResponse> getVideos(Long projectId, VideoSearchRequest searchParams) {
        UUID userId = currentUserProvider.getCurrentUserId();
        projectService.assertProjectOwnedByUser(projectId, userId);

        String formattedSearch = (searchParams.searchTerm() == null || searchParams.searchTerm().isBlank())
                ? null
                : "%" + searchParams.searchTerm().trim() + "%";

        return videoRepository
                .findProjectVideos(projectId, formattedSearch, searchParams.status(), searchParams.toPageable())
                .map(videoMapper::toVideoResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VideoResponse getVideoById(Long videoId) {
        UUID  userId = currentUserProvider.getCurrentUserId();
        Video video  = findVideoById(videoId);
        projectService.assertProjectOwnedByUser(video.getProjectId(), userId);
        return videoMapper.toVideoResponse(video);
    }

    @Override
    @Transactional
    public void deleteVideo(Long videoId) {
        UUID  userId = currentUserProvider.getCurrentUserId();
        Video video  = findVideoById(videoId);
        projectService.assertProjectOwnedByUser(video.getProjectId(), userId);

        storagePort.delete(video.getPublicId());
        videoRepository.delete(video);
    }

    @Override
    @Transactional(readOnly = true)
    public void assertVideoOwnedByUser(Long videoId, UUID userId) {
        Video video = findVideoById(videoId);
        projectService.assertProjectOwnedByUser(video.getProjectId(), userId);
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    private Video findVideoById(Long videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException(videoId));
    }
}

