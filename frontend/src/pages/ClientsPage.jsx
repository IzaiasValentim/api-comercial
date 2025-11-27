import React, { useEffect, useState, useCallback } from 'react';
import api from '../services/api';
import Sidebar from '../components/Sidebar';
import ClientFilters from '../components/ClientFilters';
import ClientsTable from '../components/ClientsTable';
import ClientFormModal from '../components/ClientFormModal';

export const ClientsPage = () => {
  // --- ESTADOS ---
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  // Filtros
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState(true); // true = Ativos, false = Inativos

  // Modal e Formulário
  const [showModal, setShowModal] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    identificationNumber: '', // CPF/CNPJ
    address: '',
    phoneNumber: '',
    phoneNumberReserve: '',
    payment: 'Dinheiro', // Valor padrão
    status: 'Active'
  });

  // --- BUSCAR CLIENTES ---
  const fetchClients = useCallback(async () => {
    try {
      setLoading(true);
      // O backend exige 'name' e 'status'
      const response = await api.get(`/api/clients/findByNameAndStatus`, {
        params: {
          name: searchTerm,
          status: statusFilter
        }
      });
      setClients(response.data);
      setError('');
    } catch (err) {
      console.error(err);
      // Se der 404 é porque não achou ninguém com esse filtro, não é erro crítico
      if (err.response && err.response.status === 404) {
        setClients([]);
      } else {
        setError('Erro ao buscar clientes.');
      }
    } finally {
      setLoading(false);
    }
  }, [searchTerm, statusFilter]);

  // Debounce para a busca (espera o usuário parar de digitar)
  useEffect(() => {
    const delayDebounceFn = setTimeout(() => {
      fetchClients();
    }, 500);

    return () => clearTimeout(delayDebounceFn);
  }, [fetchClients]);

  // --- MANIPULAÇÃO DO FORMULÁRIO ---
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const openNewClientModal = () => {
    setIsEditing(false);
    setFormData({
      name: '', email: '', identificationNumber: '', address: '',
      phoneNumber: '', phoneNumberReserve: '', payment: 'Dinheiro', status: 'Active'
    });
    setShowModal(true);
  };

  const openEditModal = (client) => {
    setIsEditing(true);
    setFormData({
      name: client.name,
      email: client.email,
      identificationNumber: client.identificationNumber,
      address: client.address,
      phoneNumber: client.phoneNumber,
      phoneNumberReserve: client.phoneNumberReserve,
      payment: client.payment || 'Dinheiro',
      status: client.status
    });
    setShowModal(true);
  };

  // --- SALVAR (CRIAR OU EDITAR) ---
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (isEditing) {
        // PUT /api/clients/
        await api.put('/api/clients/', formData);
        alert('Cliente atualizado com sucesso!');
      } else {
        // POST /api/clients/
        await api.post('/api/clients/', formData);
        alert('Solicitação de cadastro enviada com sucesso!');
      }
      setShowModal(false);
      fetchClients();
    } catch (err) {
      console.error(err);
      alert('Erro ao salvar cliente. Verifique se o CPF/CNPJ já existe ou se você tem permissão.');
    }
  };

  // --- AÇÕES EXTRAS (APROVAR / EXCLUIR) ---

  const handleApprove = async (cpf) => {
    try {
      // PUT /api/clients/approveClient/
      await api.put('/api/clients/approveClient/', { identificationNumber: cpf });
      alert('Cliente aprovado!');
      fetchClients();
    } catch (err) {
      alert('Erro ao aprovar. Apenas Gerentes podem fazer isso.');
    }
  };

  const handleDelete = async (cpf) => {
    if (window.confirm(`Deseja remover o cliente portador do CPF/CNPJ ${cpf}?`)) {
      try {
        // DELETE /api/clients/?identificationNumber=...
        await api.delete(`/api/clients/?identificationNumber=${cpf}`);
        alert('Cliente removido!');
        fetchClients();
      } catch (err) {
        alert('Erro ao remover. Apenas Gerentes podem fazer isso.');
      }
    }
  };

  return (
    <div className="flex h-screen bg-light-gray">
      <Sidebar />

      <main className="flex-1 overflow-y-auto p-8 relative">
        <div className="flex flex-col md:flex-row justify-between items-center mb-6 gap-4">
          <h2 className="text-3xl font-bold text-dark-gray">Gerir Clientes</h2>
          <button
            onClick={openNewClientModal}
            className="bg-primary hover:bg-primary-hover text-white font-bold py-2 px-4 rounded transition-colors"
          >
            + Novo Cliente
          </button>
        </div>

        <ClientFilters
          searchTerm={searchTerm}
          setSearchTerm={setSearchTerm}
          statusFilter={statusFilter}
          setStatusFilter={setStatusFilter}
        />

        <ClientsTable
          clients={clients}
          loading={loading}
          error={error}
          handleApprove={handleApprove}
          openEditModal={openEditModal}
          handleDelete={handleDelete}
        />

        <ClientFormModal
          showModal={showModal}
          setShowModal={setShowModal}
          isEditing={isEditing}
          formData={formData}
          handleInputChange={handleInputChange}
          handleSubmit={handleSubmit}
        />
      </main>
    </div>
  );
};