package com.elevideo.backend.processing.api;

import com.elevideo.backend.processing.api.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * API pública del módulo processing.
 */
public interface ProcessingService {

    VideoJobCreatedResponse processVideo(Long videoId, VideoProcessRequest request, String baseUrl);

    JobResponse getJobStatus(Long videoId, String jobId);

    VideoJobCancelResponse cancelJob(Long videoId, String jobId);

    Page<VideoRenditionResponse> getRenditions(Long videoId, VideoRenditionSearchRequest request);

    VideoRenditionResponse getRenditionById(Long videoId, Long renditionId);

    void deleteRendition(Long videoId, Long renditionId);

    Page<JobResponse> listJobs(Long videoId, JobSearchRequest request);

    JobOverviewResponse getOverview(Long videoId);
}
