const API_URL = import.meta.env.VITE_API_URL; // En Netlify: /api

const getAuthHeaders = () => {
  const token = localStorage.getItem("token");
  return token ? { "Authorization": `Bearer ${token}` } : {};
};

// --- LOGIN ---
export const loginUser = async (email, password) => {
  const response = await fetch(`${API_URL}/v1/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, password }),
  });

  if (!response.ok) throw new Error("Credenciales inválidas");
  
  // Tu Java devuelve un ApiResult, el token suele estar en result.data
  const result = await response.json();
  return result.data; 
};

// Actualiza la función registerUser en api.js
export const registerUser = async (firstName, lastName, email, password) => {
  const response = await fetch(`${import.meta.env.VITE_API_URL}/v1/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ 
      firstName: firstName, 
      lastName: lastName,   
      email: email, 
      password: password 
    }),
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    // Mostramos el mensaje exacto que devuelva Java (ej. "La contraseña es débil")
    throw new Error(errorData.message || "Error al registrar el usuario");
  }

  return response.json();
};

// --- OBTENER MI PERFIL (Ajustado a tu UserController.java) ---
export const getMyProfile = async () => {
  const response = await fetch(`${API_URL}/users/me`, {
    method: "GET",
    headers: { ...getAuthHeaders() },
  });

  if (!response.ok) throw new Error("No se pudo obtener el perfil");
  
  const result = await response.json();
  return result.data; // Retornamos result.data porque usas ApiResult en Java
};

// --- OBTENER VIDEOS ---
export const getVideos = async (projectId = 1) => {
  const response = await fetch(`${API_URL}/v1/projects/${projectId}/videos`, {
    method: "GET",
    headers: { ...getAuthHeaders() },
  });

  if (!response.ok) throw new Error("Error obteniendo videos");
  
  const result = await response.json();
  // Como usas Page de Spring Data, los videos están en result.data.content
  return result.data?.content || []; 
};