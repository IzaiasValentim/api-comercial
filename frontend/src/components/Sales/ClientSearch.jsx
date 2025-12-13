import React, { useState } from 'react';
import api from '../../services/api';

const ClientSearch = ({ onSelectClient }) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchTerm.trim()) return;

        setLoading(true);
        setError('');
        setClients([]);

        try {
            // Tenta buscar por nome e status ativo
            const response = await api.get(`/api/clients/findByNameAndStatus?name=${searchTerm}&status=true`);
            setClients(response.data);
            if (response.data.length === 0) {
                setError('Nenhum cliente encontrado.');
            }
        } catch (err) {
            console.error(err);
            setError('Erro ao buscar clientes.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="bg-white p-4 rounded shadow mb-4">
            <h3 className="font-bold text-lg mb-2 text-dark-gray">1. Selecionar Cliente</h3>
            <form onSubmit={handleSearch} className="flex gap-2 mb-4">
                <input
                    type="text"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder="Nome do cliente..."
                    className="flex-1 p-2 border rounded"
                />
                <button
                    type="submit"
                    disabled={loading}
                    className="bg-primary text-white px-4 py-2 rounded hover:bg-primary-hover disabled:opacity-50"
                >
                    {loading ? 'Buscando...' : 'Buscar'}
                </button>
            </form>

            {error && <p className="text-red-500 text-sm mb-2">{error}</p>}

            {clients.length > 0 && (
                <div className="max-h-40 overflow-y-auto border rounded">
                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-100">
                            <tr>
                                <th className="p-2">Nome</th>
                                <th className="p-2">CPF</th>
                                <th className="p-2 text-right">Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {clients.map(client => (
                                <tr key={client.identificationNumber} className="border-t hover:bg-gray-50">
                                    <td className="p-2">{client.name}</td>
                                    <td className="p-2 font-mono">{client.identificationNumber}</td>
                                    <td className="p-2 text-right">
                                        <button
                                            onClick={() => onSelectClient(client)}
                                            className="text-primary hover:underline font-bold"
                                        >
                                            Selecionar
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default ClientSearch;
