import React, { createContext, useState, useContext, useEffect } from 'react';
import api from '../services/api';

const AuthContext = createContext();

const decodeToken = (token) => {
  try {
    if (!token) return null;
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error("Erro ao decodificar token:", error);
    return null;
  }
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const recoveredToken = localStorage.getItem('authToken');
    
    if (recoveredToken) {
      const decoded = decodeToken(recoveredToken);
      if (decoded && decoded.sub) {
        const rawRole = decoded.scope || ''; 
        const role = rawRole.replace('SCOPE_', ''); 

        return { 
          username: decoded.sub,
          role: role
        };
      }
    }
    return null;
  });

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const recoveredToken = localStorage.getItem('authToken');
    if (recoveredToken) {
      api.defaults.headers.common['Authorization'] = `Bearer ${recoveredToken}`;
    }
  }, []);

  const login = async (username, password) => {
    setLoading(true);
    try {
      const response = await api.post('/authenticate/login/', { 
        username,
        password
      });

      const { token, refreshToken } = response.data;

      localStorage.setItem('authToken', token);
      localStorage.setItem('refreshToken', refreshToken);
      
      api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      const decoded = decodeToken(token);
      const rawRole = decoded.scope || '';
      const role = rawRole.replace('SCOPE_', '');

      setUser({ 
        username,
        role: role
      }); 
      
      return true;
    } catch (error) {
      console.error("Erro login:", error);
      return false;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('authToken');
    localStorage.removeItem('refreshToken');
    delete api.defaults.headers.common['Authorization'];
  };

  const hasPermission = (allowedRoles) => {
    if (!user || !user.role) return false;
    return allowedRoles.includes(user.role);
  };

  return (
    <AuthContext.Provider value={{ 
      authenticated: !!user, 
      user, 
      loading, 
      login, 
      logout,
      hasPermission
    }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => useContext(AuthContext);