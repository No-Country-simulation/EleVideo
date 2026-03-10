import { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authApi } from '@/api/auth';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedToken = localStorage.getItem('elevideo_token');
    const storedUser = localStorage.getItem('elevideo_user');
    
    if (storedToken && storedUser) {
      setToken(storedToken);
      setUser(JSON.parse(storedUser));
    }
    setIsLoading(false);
  }, []);

  const login = useCallback(async (credentials) => {
    const response = await authApi.login(credentials);
    const { token: newToken } = response.data;
    
    localStorage.setItem('elevideo_token', newToken);
    setToken(newToken);
    
    // Fetch user data
    const userResponse = await authApi.getMe();
    const userData = userResponse.data || userResponse;
    localStorage.setItem('elevideo_user', JSON.stringify(userData));
    setUser(userData);
    
    return response;
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('elevideo_token');
    localStorage.removeItem('elevideo_user');
    setToken(null);
    setUser(null);
  }, []);

  const updateUser = useCallback((userData) => {
    setUser(userData);
    localStorage.setItem('elevideo_user', JSON.stringify(userData));
  }, []);

  const value = {
    user,
    token,
    isAuthenticated: !!token,
    isLoading,
    login,
    logout,
    updateUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
