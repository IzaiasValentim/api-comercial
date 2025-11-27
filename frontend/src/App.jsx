// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { UsersPage } from './pages/UsersPage';
import { ProductsPage } from './pages/ProductsPage';
import { ClientsPage } from './pages/ClientsPage';

// Componente para proteger rotas privadas
const PrivateRoute = ({ children }) => {
  const { authenticated, loading } = useAuth();

  if (loading) {
    return <div className="flex h-screen items-center justify-center">Carregando...</div>;
  }

  if (!authenticated) {
    return <Navigate to="/login" />;
  }

  return children;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          {/* Rota Pública */}
          <Route path="/login" element={<LoginPage />} />

          {/* Rota Privada */}
          <Route 
            path="/dashboard" 
            element={
              <PrivateRoute>
                <DashboardPage />
              </PrivateRoute>
            } 
          />

          {/* ROTA DE USUÁRIOS */}
          <Route 
            path="/users" 
            element={
              <PrivateRoute>
                <UsersPage />
              </PrivateRoute>
            } 
          />

          {/* ROTA DE PRODUTOS */}
          <Route 
            path="/products" 
            element={
              <PrivateRoute>
                <ProductsPage />
              </PrivateRoute>
            } 
          />

          {/* ROTA PARA GESTÃO DE CLIENTES */}
          <Route 
            path="/clients" 
            element={
              <PrivateRoute>
                <ClientsPage />
              </PrivateRoute>
            } 
          />

          {/* Redirecionar raiz para dashboard (o PrivateRoute vai jogar pro login se precisar) */}
          <Route path="*" element={<Navigate to="/dashboard" />} />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;