import apiClient from './client';

export const processingApi = {
  // POST /api/v1/projects/{projectId}/videos/{videoId}/jobs - Iniciar job de procesamiento
  createJob: async (projectId, videoId, processData) => {
    const response = await apiClient.post(
      `/api/v1/projects/${projectId}/videos/${videoId}/jobs`,
      processData
    );
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId}/jobs - Listar jobs del video
  getJobs: async (projectId, videoId, params = {}) => {
    const response = await apiClient.get(
      `/api/v1/projects/${projectId}/videos/${videoId}/jobs`,
      { params }
    );
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId}/jobs/{jobId} - Obtener estado de un job
  getJobStatus: async (projectId, videoId, jobId) => {
    const response = await apiClient.get(
      `/api/v1/projects/${projectId}/videos/${videoId}/jobs/${jobId}`
    );
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId}/jobs/overview - Resumen de jobs
  getJobsOverview: async (projectId, videoId) => {
    const response = await apiClient.get(
      `/api/v1/projects/${projectId}/videos/${videoId}/jobs/overview`
    );
    return response.data;
  },

  // POST /api/v1/projects/{projectId}/videos/{videoId}/jobs/{jobId}/cancel - Cancelar job
  cancelJob: async (projectId, videoId, jobId) => {
    const response = await apiClient.post(
      `/api/v1/projects/${projectId}/videos/${videoId}/jobs/${jobId}/cancel`
    );
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId}/renditions - Listar renditions del video
  getRenditions: async (projectId, videoId, params = {}) => {
    const response = await apiClient.get(
      `/api/v1/projects/${projectId}/videos/${videoId}/renditions`,
      { params }
    );
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId}/renditions/{renditionId} - Obtener rendition por ID
  getRenditionById: async (projectId, videoId, renditionId) => {
    const response = await apiClient.get(
      `/api/v1/projects/${projectId}/videos/${videoId}/renditions/${renditionId}`
    );
    return response.data;
  },

  // DELETE /api/v1/projects/{projectId}/videos/{videoId}/renditions/{renditionId} - Eliminar rendition
  deleteRendition: async (projectId, videoId, renditionId) => {
    const response = await apiClient.delete(
      `/api/v1/projects/${projectId}/videos/${videoId}/renditions/${renditionId}`
    );
    return response.data;
  },
};
