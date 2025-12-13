import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../services/api';

export const SaleDetailsPage = () => {
    const { id } = useParams();
    const [sale, setSale] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSaleDetails = async () => {
            try {
                const response = await api.get(`/api/purchases/${id}`);
                setSale(response.data);
            } catch (err) {
                console.error("Erro ao carregar detalhes da venda:", err);
                setError("Não foi possível carregar os detalhes da venda.");
            } finally {
                setLoading(false);
            }
        };

        fetchSaleDetails();
    }, [id]);

    if (loading) {
        return (
            <div className="flex h-screen items-center justify-center bg-light-gray">
                <p className="text-xl text-gray-500">Carregando detalhes...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="flex h-screen items-center justify-center bg-light-gray flex-col gap-4">
                <p className="text-xl text-red-500">{error}</p>
                <Link to="/sales" className="text-primary hover:underline">Voltar para Vendas</Link>
            </div>
        );
    }

    if (!sale) return null;

    const getStatusLabel = (status) => {

        return (
            <p>
                <button
                    enabled={false}
                    className="text-blue-600 hover:text-blue-800 text-sm font-bold border border-blue-200 px-2 py-1 rounded hover:bg-blue-50"
                    title="Status da venda"
                >
                    Status:
                     <span className="px-3 py-1 rounded-full text-sm font-bold bg-gray-100 text-gray-800 ">{status}</span>
                </button>
               
            </p>);
    };

    return (
        <div className="min-h-screen bg-light-gray p-8">
            <div className="max-w-4xl mx-auto">
                <div className="mb-6 flex items-center justify-between">
                    <div className="flex items-center gap-4">
                        <Link to="/sales" className="text-gray-500 hover:text-primary transition-colors">
                            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                            </svg>
                        </Link>
                        <h1 className="text-3xl font-bold text-dark-gray">Detalhes da Venda</h1>
                    </div>
                    {getStatusLabel(sale.status)}
                </div>

                <div className="bg-white rounded-lg shadow-md overflow-hidden mb-6">
                    <div className="p-6 border-b border-gray-100">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-1">Informações da Venda</h3>
                                <p className="text-lg font-medium text-dark-gray">ID: <span className="font-normal text-gray-600 text-base">{sale.id}</span></p>
                                <p className="text-lg font-medium text-dark-gray mt-2">Data: <span className="font-normal text-gray-600 text-base">{sale.realizationDate ? new Date(sale.realizationDate).toLocaleString() : '-'}</span></p>
                                <p className="text-lg font-medium text-dark-gray mt-2">Método de Pagamento: <span className="font-normal text-gray-600 text-base">{sale.paymentMethod}</span></p>
                            </div>
                            <div>
                                <h3 className="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-1">Envolvidos</h3>
                                <p className="text-lg font-medium text-dark-gray">Cliente: <span className="font-normal text-gray-600 text-base">{sale.clientName}</span></p>
                                <p className="text-lg font-medium text-dark-gray mt-2">Vendedor: <span className="font-normal text-gray-600 text-base">{sale.sellerUsername}</span></p>
                            </div>
                        </div>
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow-md overflow-hidden">
                    <div className="p-6 border-b border-gray-100">
                        <h2 className="text-xl font-bold text-dark-gray">Itens da Venda</h2>
                    </div>
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50">
                                <tr>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">Produto</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider">Código</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider text-right">Preço Unit.</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider text-center">Qtd.</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 uppercase tracking-wider text-right">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {sale.items && sale.items.map((item, index) => (
                                    <tr key={index} className="hover:bg-gray-50">
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.itemName}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.itemCode}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-right">
                                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.unitPrice)}
                                        </td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 text-center">{item.quantity}</td>
                                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900 text-right">
                                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(item.unitPrice * item.quantity)}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot className="bg-gray-50">
                                <tr>
                                    <td colSpan="4" className="px-6 py-4 text-right font-bold text-gray-700">Total:</td>
                                    <td className="px-6 py-4 text-right font-bold text-xl text-primary">
                                        {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(sale.total)}
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
};

