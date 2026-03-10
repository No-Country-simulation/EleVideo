import apiClient from './client';

export const authApi = {
  // POST /api/v1/auth/login
  login: async (credentials) => {
    const response = await apiClient.post('/api/v1/auth/login', credentials);
    return response.data;
  },

  // POST /api/v1/auth/register
  register: async (userData) => {
    const response = await apiClient.post('/api/v1/auth/register', userData);
    return response.data;
  },

  // POST /api/v1/auth/verify-email
  verifyEmail: async (token) => {
    const response = await apiClient.post('/api/v1/auth/verify-email', { token });
    return response.data;
  },

  // POST /api/v1/auth/forgot-password
  forgotPassword: async (email) => {
    const response = await apiClient.post('/api/v1/auth/forgot-password', { email });
    return response.data;
  },

  // POST /api/v1/auth/reset-password
  resetPassword: async (data) => {
    const response = await apiClient.post('/api/v1/auth/reset-password', data);
    return response.data;
  },

  // GET /api/v1/users/me - Obtener usuario autenticado
  getMe: async () => {
    const response = await apiClient.get('/api/v1/users/me');
    return response.data;
  },
};
