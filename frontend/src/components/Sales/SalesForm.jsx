import React, { useState } from 'react';
import api from '../../services/api';
import ClientSearch from './ClientSearch';
import ProductSearch from './ProductSearch';
import Cart from './Cart';

const SalesForm = ({ onSuccess }) => {
    const [selectedClient, setSelectedClient] = useState(null);
    const [cartItems, setCartItems] = useState([]);
    const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
    const [loading, setLoading] = useState(false);

    const handleSelectClient = (client) => {
        setSelectedClient(client);
    };

    const handleAddToCart = (product, quantity) => {
        setCartItems(prev => {
            const existing = prev.find(item => item.code === product.code);
            if (existing) {
                return prev.map(item =>
                    item.code === product.code
                        ? { ...item, quantity: item.quantity + quantity }
                        : item
                );
            }
            return [...prev, { ...product, quantity }];
        });
    };

    const handleRemoveFromCart = (code) => {
        setCartItems(prev => prev.filter(item => item.code !== code));
    };

    const handleUpdateQuantity = (code, newQuantity) => {
        if (newQuantity <= 0) return;
        setCartItems(prev => prev.map(item =>
            item.code === code ? { ...item, quantity: newQuantity } : item
        ));
    };

    const handleSubmit = async () => {
        if (!selectedClient) {
            alert("Selecione um cliente.");
            return;
        }
        if (cartItems.length === 0) {
            alert("O carrinho está vazio.");
            return;
        }

        if (!window.confirm("Confirma a criação da venda?")) return;

        setLoading(true);
        try {
            const payload = {
                paymentMethod,
                clientIdentificationNumber: selectedClient.identificationNumber,
                items: cartItems.map(item => ({
                    code: item.code,
                    quantity: item.quantity
                }))
            };

            await api.post('/api/purchases', payload);
            alert("Venda realizada com sucesso!");
            onSuccess(); // Callback to switch tab or refresh
        } catch (error) {
            console.error("Erro ao realizar venda:", error);
            alert("Erro ao realizar venda. Verifique os dados e tente novamente.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            {/* 1. Client Selection */}
            <div className="bg-white p-4 rounded shadow">
                <div className="flex justify-between items-center mb-2">
                    <h3 className="font-bold text-lg text-dark-gray">Cliente</h3>
                    {selectedClient && (
                        <button
                            onClick={() => setSelectedClient(null)}
                            className="text-sm text-red-500 hover:underline"
                        >
                            Alterar
                        </button>
                    )}
                </div>

                {selectedClient ? (
                    <div className="bg-green-50 p-3 rounded border border-green-200">
                        <p className="font-bold text-green-800">{selectedClient.name}</p>
                        <p className="text-sm text-green-600">CPF: {selectedClient.identificationNumber}</p>
                    </div>
                ) : (
                    <ClientSearch onSelectClient={handleSelectClient} />
                )}
            </div>

            {/* 2. Product Search & Cart (Only if client selected) */}
            {selectedClient && (
                <>
                    <ProductSearch onAddToCart={handleAddToCart} />

                    <Cart
                        items={cartItems}
                        onRemoveItem={handleRemoveFromCart}
                        onUpdateQuantity={handleUpdateQuantity}
                    />

                    {/* 3. Payment & Submit */}
                    <div className="bg-white p-4 rounded shadow">
                        <h3 className="font-bold text-lg mb-4 text-dark-gray">4. Finalizar Venda</h3>

                        <div className="mb-4">
                            <label className="block text-sm font-medium mb-1">Método de Pagamento</label>
                            <select
                                value={paymentMethod}
                                onChange={(e) => setPaymentMethod(e.target.value)}
                                className="w-full p-2 border rounded"
                            >
                                <option value="CREDIT_CARD">Cartão de Crédito</option>
                                <option value="DEBIT_CARD">Cartão de Débito</option>
                                <option value="CASH">Dinheiro</option>
                                <option value="PIX">PIX</option>
                            </select>
                        </div>

                        <div className="flex justify-end">
                            <button
                                onClick={handleSubmit}
                                disabled={loading || cartItems.length === 0}
                                className="bg-green-600 text-white px-6 py-3 rounded font-bold text-lg hover:bg-green-700 disabled:opacity-50"
                            >
                                {loading ? 'Processando...' : 'Confirmar Venda'}
                            </button>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};

export default SalesForm;
