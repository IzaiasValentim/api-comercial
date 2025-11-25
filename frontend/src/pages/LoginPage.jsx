// src/pages/LoginPage.jsx

import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

export const LoginPage = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const { login, loading, authenticated } = useAuth();

  const navigate = useNavigate();

  useEffect(() => {
    if (authenticated) {
      navigate('/dashboard');
    }
  }, [authenticated, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const sucesso = await login(username, password);
    
    if (sucesso) {
      navigate('/dashboard');
    } else {
      alert("Falha no login. Verifique usuário e senha.");
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-light-gray">
      <div className="w-full max-w-md rounded-lg bg-white p-8 shadow-lg">
        <h2 className="mb-6 text-center text-3xl font-bold text-primary">ClickComercial</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-dark-gray">Usuário</label>
            <input 
              type="text" 
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="mt-1 w-full rounded border border-medium-gray p-2 focus:border-primary focus:ring-primary"
              placeholder="Ex: ADM"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-dark-gray">Senha</label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="mt-1 w-full rounded border border-medium-gray p-2 focus:border-primary focus:ring-primary"
              placeholder="********"
            />
          </div>

          <button
              type="submit"
              disabled={loading}
              className={`w-full rounded-md p-3 text-white transition-colors ${
                loading
                  ? 'cursor-not-allowed bg-medium-gray'
                  : 'bg-primary hover:bg-primary-hover'
              }`}
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
        </form>
      </div>
    </div>
  );
};