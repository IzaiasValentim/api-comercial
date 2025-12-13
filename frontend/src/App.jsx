// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { UsersPage } from './pages/UsersPage';
import { ProductsPage } from './pages/ProductsPage';
import { ClientsPage } from './pages/ClientsPage';
import { SalesPage } from './pages/SalesPage';
import { SaleDetailsPage } from './pages/SaleDetailsPage';
import { RoleRoute } from './components/RoleRoute';

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
               <RoleRoute roles={['MANAGER', 'SELLER', 'INTERN']}>
                <DashboardPage />
              </RoleRoute>
            }
          />

          {/* ROTA DE USUÁRIOS */}
          <Route
            path="/users"
            element={
              <RoleRoute roles={['MANAGER', 'ADMINISTRATOR']}>
                <UsersPage />
              </RoleRoute>
            }
          />

          {/* ROTA DE PRODUTOS */}
          <Route
            path="/products"
            element={
              <RoleRoute roles={['MANAGER', 'SELLER', 'INTERN']}>
                <ProductsPage />
              </RoleRoute>
            }
          />

          {/* ROTA PARA GESTÃO DE CLIENTES */}
          <Route
            path="/clients"
            element={
              <RoleRoute roles={['MANAGER', 'SELLER']}>
                <ClientsPage />
              </RoleRoute>
            }
          />

          {/* ROTA DE VENDAS */}
          <Route
            path="/sales"
            element={
              <RoleRoute roles={['MANAGER', 'SELLER', 'INTERN']}>
                <SalesPage />
              </RoleRoute>
            }
          />
          {/* ROTA DE DETALHES DA VENDA */}
          <Route
            path="/sales/:id"
            element={
             <RoleRoute roles={['MANAGER', 'SELLER', 'INTERN']}>
                <SaleDetailsPage />
              </RoleRoute>
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