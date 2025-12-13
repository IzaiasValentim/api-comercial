import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export const RoleRoute = ({ children, roles }) => {
  const { authenticated, loading, hasPermission } = useAuth();

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Carregando...</div>;
  }

  if (!authenticated) {
    return <Navigate to="/login" />;
  }


  if (roles && !hasPermission(roles)) {
    alert("Acesso negado: Você não tem permissão para acessar esta página.");
    return <Navigate to="/dashboard" />;
  }

  return children;
};