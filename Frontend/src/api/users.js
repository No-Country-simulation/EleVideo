import apiClient from './client';

export const usersApi = {
  // GET /api/v1/users/me - Obtener usuario autenticado
  getMe: async () => {
    const response = await apiClient.get('/api/v1/users/me');
    return response.data;
  },

  // PATCH /api/v1/users/me - Actualizar perfil
  update: async (userData) => {
    const response = await apiClient.patch('/api/v1/users/me', userData);
    return response.data;
  },

  // PATCH /api/v1/users/me/password - Cambiar contraseña
  changePassword: async (passwords) => {
    const response = await apiClient.patch('/api/v1/users/me/password', passwords);
    return response.data;
  },

  // DELETE /api/v1/users/me - Eliminar cuenta
  delete: async () => {
    const response = await apiClient.delete('/api/v1/users/me');
    return response.data;
  },
};
