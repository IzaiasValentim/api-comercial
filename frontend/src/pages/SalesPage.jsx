import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import SalesList from '../components/Sales/SalesList';
import SalesForm from '../components/Sales/SalesForm';
import { useAuth } from '../contexts/AuthContext';

export const SalesPage = () => {
    
    const [activeTab, setActiveTab] = useState('list'); // 'list' or 'create'
    const { hasPermission } = useAuth();

    const handleSaleSuccess = () => {
        setActiveTab('list');
    };

    return (
        <div className="flex h-screen bg-light-gray">
            {/* Sidebar (Reused structure, but ideally should be a layout component) */}
            <aside className="w-64 bg-white shadow-md hidden md:block">
                <div className="p-6">
                    <h1 className="text-2xl font-bold text-primary">ClickComercial</h1>
                </div>
                <nav className="mt-6 px-4 space-y-2">
                    <Link to="/dashboard" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Voltar ao Dashboard</Link>
                </nav>
            </aside>

            <main className="flex-1 overflow-y-auto p-8 relative">
                <div className="flex justify-between items-center mb-6">
                    <h2 className="text-3xl font-bold text-dark-gray">Gestão de Vendas</h2>
                </div>

                {/* Tabs */}
                <div className="flex border-b border-gray-200 mb-6">
                    <button
                        className={`py-2 px-4 font-medium ${activeTab === 'list' ? 'text-primary border-b-2 border-primary' : 'text-gray-500 hover:text-gray-700'}`}
                        onClick={() => setActiveTab('list')}
                    >
                        Histórico de Vendas
                    </button>

                    {/* O botão só aparece se for GERENTE ou VENDEDOR */}
                    {hasPermission(['MANAGER', 'SELLER']) && (
                    <button
                        className={`py-2 px-4 font-medium ${activeTab === 'create' ? 'text-primary border-b-2 border-primary' : 'text-gray-500 hover:text-gray-700'}`}
                        onClick={() => setActiveTab('create')}
                    >
                        Nova Venda
                    </button>
                    )}
                    
                </div>

                {/* Content */}
                <div>
                    {activeTab === 'list' ? (
                        <SalesList />
                    ) : (
                        <SalesForm onSuccess={handleSaleSuccess} />
                    )}
                </div>
            </main>
        </div>
    );
};
