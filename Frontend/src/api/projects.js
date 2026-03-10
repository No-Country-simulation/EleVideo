import apiClient from './client';

export const projectsApi = {
  // GET /api/v1/projects - Listar proyectos
  getAll: async (params = {}) => {
    const response = await apiClient.get('/api/v1/projects', { params });
    return response.data;
  },

  // GET /api/v1/projects/{projectId} - Obtener proyecto por ID
  getById: async (projectId) => {
    const response = await apiClient.get(`/api/v1/projects/${projectId}`);
    return response.data;
  },

  // POST /api/v1/projects - Crear proyecto
  create: async (projectData) => {
    const response = await apiClient.post('/api/v1/projects', projectData);
    return response.data;
  },

  // PUT /api/v1/projects/{projectId} - Actualizar proyecto
  update: async (projectId, projectData) => {
    const response = await apiClient.put(`/api/v1/projects/${projectId}`, projectData);
    return response.data;
  },

  // DELETE /api/v1/projects/{projectId} - Eliminar proyecto
  delete: async (projectId) => {
    const response = await apiClient.delete(`/api/v1/projects/${projectId}`);
    return response.data;
  },
};
