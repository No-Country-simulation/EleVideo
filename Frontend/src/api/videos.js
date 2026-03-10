import apiClient from './client';

export const videosApi = {
  // GET /api/v1/projects/{projectId}/videos - Listar videos del proyecto
  getByProject: async (projectId, params = {}) => {
    const response = await apiClient.get(`/api/v1/projects/${projectId}/videos`, { params });
    return response.data;
  },

  // GET /api/v1/projects/{projectId}/videos/{videoId} - Obtener video por ID
  getById: async (projectId, videoId) => {
    const response = await apiClient.get(`/api/v1/projects/${projectId}/videos/${videoId}`);
    return response.data;
  },

  // POST /api/v1/projects/{projectId}/videos - Subir video (multipart/form-data)
  create: async (projectId, formData) => {
    const response = await apiClient.post(`/api/v1/projects/${projectId}/videos`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  },

  // DELETE /api/v1/projects/{projectId}/videos/{videoId} - Eliminar video
  delete: async (projectId, videoId) => {
    const response = await apiClient.delete(`/api/v1/projects/${projectId}/videos/${videoId}`);
    return response.data;
  },
};
