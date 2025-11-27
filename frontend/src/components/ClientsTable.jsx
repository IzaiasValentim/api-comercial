import React from 'react';

const ClientsTable = ({ clients, loading, error, handleApprove, openEditModal, handleDelete }) => {
    if (loading) {
        return <p className="text-medium-gray">Carregando clientes...</p>;
    }

    if (error) {
        return <div className="bg-red-100 text-red-700 p-4 rounded">{error}</div>;
    }

    return (
        <div className="overflow-x-auto rounded-lg bg-white shadow">
            <table className="w-full text-left border-collapse">
                <thead>
                    <tr className="bg-secondary text-primary">
                        <th className="p-4 font-semibold">Nome</th>
                        <th className="p-4 font-semibold">CPF/CNPJ</th>
                        <th className="p-4 font-semibold">Email</th>
                        <th className="p-4 font-semibold">Telefone</th>
                        <th className="p-4 font-semibold text-center">Status</th>
                        <th className="p-4 font-semibold text-center">Ações</th>
                    </tr>
                </thead>
                <tbody>
                    {clients.length > 0 ? clients.map((client) => (
                        <tr key={client.identificationNumber} className="border-b border-light-gray hover:bg-gray-50">
                            <td className="p-4 font-medium text-dark-gray">{client.name}</td>
                            <td className="p-4 text-medium-gray font-mono text-sm">{client.identificationNumber}</td>
                            <td className="p-4 text-medium-gray">{client.email}</td>
                            <td className="p-4 text-medium-gray">{client.phoneNumber}</td>
                            <td className="p-4 text-center">
                                {client.status === 'Active' ? (
                                    <span className="px-2 py-1 rounded-full text-xs font-bold bg-green-100 text-green-800">Ativo</span>
                                ) : (
                                    <button
                                        onClick={() => handleApprove(client.identificationNumber)}
                                        className="px-2 py-1 rounded-full text-xs font-bold bg-yellow-100 text-yellow-800 hover:bg-yellow-200 cursor-pointer"
                                        title="Clique para aprovar (Gerentes)"
                                    >
                                        Pendente ⏳
                                    </button>
                                )}
                            </td>
                            <td className="p-4 text-center flex justify-center gap-2">
                                <button
                                    onClick={() => openEditModal(client)}
                                    className="text-blue-600 hover:text-blue-800 font-medium text-sm"
                                >
                                    Editar
                                </button>
                                <button
                                    onClick={() => handleDelete(client.identificationNumber)}
                                    className="text-red-500 hover:text-red-700 font-medium text-sm ml-2"
                                >
                                    Excluir
                                </button>
                            </td>
                        </tr>
                    )) : (
                        <tr><td colSpan="6" className="p-8 text-center text-medium-gray">Nenhum cliente encontrado.</td></tr>
                    )}
                </tbody>
            </table>
        </div>
    );
};

export default ClientsTable;
