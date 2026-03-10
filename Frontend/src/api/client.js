import axios from 'axios';

// Nueva URL del backend
const API_BASE_URL = 'https://elevideo-ec.onrender.com';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add JWT token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('elevideo_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle auth errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    // Solo redirigir al login si el error es 401 Y no es una petición de login/register
    if (error.response?.status === 401) {
      const isAuthRequest = error.config?.url?.includes('/auth/');
      if (!isAuthRequest) {
        localStorage.removeItem('elevideo_token');
        localStorage.removeItem('elevideo_user');
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default apiClient;
