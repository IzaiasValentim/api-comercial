// src/pages/DashboardPage.jsx
import React from 'react';
import { useAuth } from '../contexts/AuthContext';
import { Link } from 'react-router-dom';


export const DashboardPage = () => {
  const { logout, user } = useAuth();

  return (
    <div className="flex h-screen bg-light-gray">
      {/* Sidebar */}
      <aside className="w-64 bg-white shadow-md">
        <div className="p-6">
          <h1 className="text-2xl font-bold text-primary">ClickComercial</h1>
        </div>
        
        <nav className="mt-6 px-4 space-y-2">
          <Link to="/dashboard" className="block rounded-md bg-secondary px-4 py-2 text-primary font-medium hover:bg-blue-100">
            Visão Geral
          </Link>
          
          {/* Novo Link para Usuários */}
          <Link to="/users" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100 hover:text-primary transition-colors">
            Usuários
          </Link>

          <Link to="/products" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100 hover:text-primary transition-colors">
            Produtos
          </Link>
          
          <Link to="/clients" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100 hover:text-primary transition-colors">
            Clientes
          </Link>

          <Link to="/sales" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100 hover:text-primary transition-colors">
            Vendas
          </Link>
        </nav>

        <div className="absolute bottom-0 w-64 p-4 border-t border-gray-200">
          <button 
            onClick={logout}
            className="w-full rounded-md border border-red-500 px-4 py-2 text-red-500 hover:bg-red-50 transition-colors"
          >
            Sair
          </button>
        </div>
      </aside>

      {/* Conteúdo Principal */}
      <main className="flex-1 overflow-y-auto p-8">
        <header className="flex items-center justify-between mb-8">
          <h2 className="text-3xl font-bold text-dark-gray">Dashboard</h2>
          <div className="flex items-center gap-2">
             <span className="text-medium-gray">Bem-vindo(a),</span>
             <span className="font-bold text-primary">{user?.username || 'Visitante'}</span>
          </div>
        </header>

        {/* Cards de Exemplo */}
        <div className="grid grid-cols-1 gap-6 md:grid-cols-3">
          <div className="rounded-lg bg-white p-6 shadow-sm border-l-4 border-primary">
            <h3 className="text-sm font-medium text-medium-gray">Total de Vendas</h3>
            <p className="mt-2 text-3xl font-bold text-dark-gray">R$ 1.250,00</p>
          </div>
          
          <div className="rounded-lg bg-white p-6 shadow-sm border-l-4 border-green-500">
            <h3 className="text-sm font-medium text-medium-gray">Produtos Ativos</h3>
            <p className="mt-2 text-3xl font-bold text-dark-gray">45</p>
          </div>

          <div className="rounded-lg bg-white p-6 shadow-sm border-l-4 border-orange-500">
            <h3 className="text-sm font-medium text-medium-gray">Clientes</h3>
            <p className="mt-2 text-3xl font-bold text-dark-gray">12</p>
          </div>
        </div>
      </main>
    </div>
  );
};