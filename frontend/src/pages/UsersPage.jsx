import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Link } from 'react-router-dom';

export const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Busca os dados assim que a tela carrega
  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await api.get('/api/users/all');
      setUsers(response.data);
    } catch (err) {
      console.error(err);
      setError('Erro ao carregar usuários. Verifique se você tem permissão (ADMIN/MANAGER).');
    } finally {
      setLoading(false);
    }
  };

  // Função para deletar (bônus)
  const handleDelete = async (username) => {
    if (window.confirm(`Tem certeza que deseja excluir ${username}?`)) {
      try {
        await api.delete(`/api/users?username=${username}`);
        fetchUsers(); // Recarrega a lista
        alert('Usuário removido com sucesso!');
      } catch ( err ) {
        alert('Erro ao deletar usuário.'+err.message);
      }
    }
  };

  return (
    <div className="flex h-screen bg-light-gray">
      {/* Sidebar Simplificada para esta tela (Idealmente seria um Componente separado) */}
      <aside className="w-64 bg-white shadow-md hidden md:block">
        <div className="p-6">
           <h1 className="text-2xl font-bold text-primary">ClickComercial</h1>
        </div>
        <nav className="mt-6 px-4 space-y-2">
          <Link to="/dashboard" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Voltar ao Dashboard</Link>
        </nav>
      </aside>

      <main className="flex-1 overflow-y-auto p-8">
        <h2 className="text-3xl font-bold text-dark-gray mb-6">Gerir Usuários</h2>

        {loading ? (
          <p>Carregando...</p>
        ) : error ? (
          <div className="bg-red-100 text-red-700 p-4 rounded">{error}</div>
        ) : (
          <div className="overflow-x-auto rounded-lg bg-white shadow">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-secondary text-primary">
                  <th className="p-4 font-semibold">Username</th>
                  <th className="p-4 font-semibold">Email</th>
                  <th className="p-4 font-semibold">Telefone</th>
                  <th className="p-4 font-semibold">Função (Role)</th>
                  <th className="p-4 font-semibold">Status</th>
                  <th className="p-4 font-semibold">Ações</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.username} className="border-b border-light-gray hover:bg-gray-50">
                    <td className="p-4 font-medium text-dark-gray">{user.username}</td>
                    <td className="p-4 text-medium-gray">{user.email}</td>
                    <td className="p-4 text-medium-gray">{user.phone}</td>
                    <td className="p-4">
                      {user.roles.map(role => (
                        <span key={role.id} className="mr-1 inline-block rounded-full bg-blue-100 px-2 py-1 text-xs font-semibold text-blue-800">
                          {role.name}
                        </span>
                      ))}
                    </td>
                    <td className="p-4">
                        <span className={`px-2 py-1 rounded-full text-xs font-semibold ${user.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                            {user.active ? 'Ativo' : 'Inativo'}
                        </span>
                    </td>
                    <td className="p-4">
                      <button 
                        onClick={() => handleDelete(user.username)}
                        className="text-red-500 hover:text-red-700 font-medium text-sm"
                      >
                        Excluir
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </main>
    </div>
  );
};