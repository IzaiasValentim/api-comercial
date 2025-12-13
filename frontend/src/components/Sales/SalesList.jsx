import React, { useState, useEffect, useCallback } from 'react';
import { Link } from 'react-router-dom';
import api from '../../services/api';

const SalesList = () => {
    const [sales, setSales] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const pageSize = 10;
    const status_cancelada = 5;

    /*
    const vendaStatus = {
        1: 'RECEBIDO',
        2: 'EM PROGRESSO',
        3: 'PRONTO',
        4: 'COMPLETADO',
        5: 'CANCELADO'
    };
    */

    // Filters
    const [cpfFilter, setCpfFilter] = useState('');
    const [statusFilter, setStatusFilter] = useState('');

    const fetchSales = useCallback(async () => {
        try {
            setLoading(true);
            let url = `/api/purchases?page=${page}&size=${pageSize}`;
            if (cpfFilter) url += `&cpf=${cpfFilter}`;
            if (statusFilter) url += `&status=${statusFilter}`;

            const response = await api.get(url);
            setSales(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error("Erro ao buscar vendas:", error);
            alert("Erro ao carregar vendas.");
        } finally {
            setLoading(false);
        }
    }, [page, cpfFilter, statusFilter]);

    useEffect(() => {
        fetchSales();
    }, [fetchSales]);

    const handleStatusUpdate = async (id, newStatus) => {
        console.log("Updating status for sale ID:", id, "to", newStatus);
        try {
            await api.patch(`/api/purchases/${id}/status`, { status: newStatus });
            alert("Status atualizado com sucesso!");
            fetchSales();
        } catch (error) {
            console.error("Erro ao atualizar status:", error);
            alert("Erro ao atualizar status.");
        }
    };

    const handleCancelSale = async (id) => {
        handleStatusUpdate(id, status_cancelada);
    };

    const getStatusLabel = (status) => {
        switch (status) {
            case 'PENDING': return <span className="px-2 py-1 rounded text-xs font-bold bg-yellow-100 text-yellow-800">Pendente</span>;
            case 'COMPLETED': return <span className="px-2 py-1 rounded text-xs font-bold bg-green-100 text-green-800">Concluída</span>;
            case 'CANCELED': return <span className="px-2 py-1 rounded text-xs font-bold bg-red-100 text-red-800">Cancelada</span>;
            default: return <span className="px-2 py-1 rounded text-xs font-bold bg-gray-100 text-gray-800">{status}</span>;
        }
    };

    return (
        <div className="bg-white rounded-lg shadow p-6">
            <div className="flex gap-4 mb-4">
                <input
                    type="text"
                    placeholder="Filtrar por CPF"
                    value={cpfFilter}
                    onChange={(e) => setCpfFilter(e.target.value)}
                    className="p-2 border rounded"
                />
                <select
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value)}
                    className="p-2 border rounded"
                >
                    <option value="">Todos os Status</option>
                    <option value="1">RECEBIDO</option>
                    <option value="2">EM PROGRESSO</option>
                    <option value="3">PRONTO</option>
                    <option value="4">COMPLETADO</option>
                    <option value="5">CANCELADO</option>
                </select>
                <button onClick={fetchSales} className="bg-primary text-white px-4 py-2 rounded hover:bg-primary-hover">Filtrar</button>
            </div>

            {loading ? (
                <p className="text-center text-gray-500">Carregando vendas...</p>
            ) : (
                <>
                    <div className="overflow-x-auto">
                        <table className="w-full text-left border-collapse">
                            <thead>
                                <tr className="bg-secondary text-primary">
                                    <th className="p-4 font-semibold">Data</th>
                                    <th className="p-4 font-semibold">Cliente</th>
                                    <th className="p-4 font-semibold">Vendedor</th>
                                    <th className="p-4 font-semibold">Total</th>
                                    <th className="p-4 font-semibold">Status</th>
                                    <th className="p-4 font-semibold text-center">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
                                {sales.length > 0 ? sales.map((sale) => (
                                    <tr key={sale.id} className="border-b border-light-gray hover:bg-gray-50">
                                        <td className="p-4 text-sm text-gray-600">
                                            {sale.date ? new Date(sale.date).toLocaleDateString() + ' ' + new Date(sale.date).toLocaleTimeString() : '-'}
                                        </td>
                                        <td className="p-4 font-medium text-dark-gray">
                                            {sale.clientName} <br />
                                            <span className="text-xs text-gray-400">{sale.clientCpf}</span>
                                        </td>
                                        <td className="p-4 text-sm text-gray-600">{sale.sellerName}</td>
                                        <td className="p-4 font-bold text-dark-gray">
                                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(sale.total)}
                                        </td>
                                        <td className="p-4">{getStatusLabel(sale.status)}</td>
                                        <td className="p-4 text-center flex justify-center gap-2">
                                            <Link
                                                to={`/sales/${sale.id}`}
                                                className="text-blue-600 hover:text-blue-800 text-xs font-bold border border-blue-200 px-2 py-1 rounded hover:bg-blue-50 flex items-center"
                                                title="Ver Detalhes"
                                            >
                                                Detalhes
                                            </Link>
                                            {sale.status !== 'CANCELED' && (
                                                <>
                                                    <button
                                                        onClick={() => handleStatusUpdate(sale.id, 4)}
                                                        className="text-green-600 hover:text-green-800 text-xs font-bold border border-green-200 px-2 py-1 rounded hover:bg-green-50"
                                                        title="Marcar como Concluída"
                                                    >
                                                        Concluir
                                                    </button>
                                                    <button
                                                        onClick={() => handleCancelSale(sale.id)}
                                                        className="text-red-600 hover:text-red-800 text-xs font-bold border border-red-200 px-2 py-1 rounded hover:bg-red-50"
                                                        title="Cancelar Venda"
                                                    >
                                                        Cancelar
                                                    </button>
                                                </>
                                            )}
                                        </td>
                                    </tr>
                                )) : (
                                    <tr><td colSpan="6" className="p-8 text-center text-gray-500">Nenhuma venda encontrada.</td></tr>
                                )}
                            </tbody>
                        </table>
                    </div>

                    <div className="flex justify-end gap-2 mt-4">
                        <button
                            disabled={page === 0}
                            onClick={() => setPage(page - 1)}
                            className="px-3 py-1 rounded border hover:bg-gray-100 disabled:opacity-50"
                        >
                            Anterior
                        </button>
                        <span className="px-3 py-1">Página {page + 1} de {totalPages || 1}</span>
                        <button
                            disabled={page >= totalPages - 1}
                            onClick={() => setPage(page + 1)}
                            className="px-3 py-1 rounded border hover:bg-gray-100 disabled:opacity-50"
                        >
                            Próxima
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default SalesList;
