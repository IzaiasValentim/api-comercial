import React, { useState } from 'react';
import api from '../../services/api';

const ProductSearch = ({ onAddToCart }) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [quantities, setQuantities] = useState({});

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!searchTerm.trim()) return;

        setLoading(true);
        setError('');
        setProducts([]);

        try {
            const response = await api.get(`/api/items/allByName?name=${searchTerm}`);
            setProducts(response.data);
            if (response.data.length === 0) {
                setError('Nenhum produto encontrado.');
            }
        } catch (err) {
            console.error(err);
            setError('Erro ao buscar produtos.');
        } finally {
            setLoading(false);
        }
    };

    const handleQuantityChange = (code, value) => {
        setQuantities(prev => ({ ...prev, [code]: value }));
    };

    const handleAddClick = (product) => {
        const qty = parseInt(quantities[product.code] || 1);
        if (qty <= 0) {
            alert("Quantidade deve ser maior que zero.");
            return;
        }
        if (qty > product.totalStock) {
            alert(`Estoque insuficiente. Disponível: ${product.totalStock}`);
            return;
        }

        onAddToCart(product, qty);
        // Reset quantity for this item
        setQuantities(prev => ({ ...prev, [product.code]: 1 }));
    };

    return (
        <div className="bg-white p-4 rounded shadow mb-4">
            <h3 className="font-bold text-lg mb-2 text-dark-gray">2. Buscar Produtos</h3>
            <form onSubmit={handleSearch} className="flex gap-2 mb-4">
                <input
                    type="text"
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    placeholder="Nome do produto..."
                    className="flex-1 p-2 border rounded"
                />
                <button
                    type="submit"
                    disabled={loading}
                    className="bg-primary text-white px-4 py-2 rounded hover:bg-primary-hover disabled:opacity-50"
                >
                    {loading ? '...' : 'Buscar'}
                </button>
            </form>

            {error && <p className="text-red-500 text-sm mb-2">{error}</p>}

            {products.length > 0 && (
                <div className="max-h-60 overflow-y-auto border rounded">
                    <table className="w-full text-left text-sm">
                        <thead className="bg-gray-100">
                            <tr>
                                <th className="p-2">Cód.</th>
                                <th className="p-2">Nome</th>
                                <th className="p-2">Preço</th>
                                <th className="p-2">Estoque</th>
                                <th className="p-2 w-32">Qtd</th>
                                <th className="p-2 text-right">Ação</th>
                            </tr>
                        </thead>
                        <tbody>
                            {products.map(product => (
                                <tr key={product.code} className="border-t hover:bg-gray-50">
                                    <td className="p-2 font-mono text-xs">{product.code}</td>
                                    <td className="p-2">{product.name}</td>
                                    <td className="p-2">R$ {product.price}</td>
                                    <td className="p-2">
                                        <span className={`font-bold ${product.totalStock > 0 ? 'text-green-600' : 'text-red-600'}`}>
                                            {product.totalStock}
                                        </span>
                                    </td>
                                    <td className="p-2">
                                        <input
                                            type="number"
                                            min="1"
                                            max={product.totalStock}
                                            value={quantities[product.code] || 1}
                                            onChange={(e) => handleQuantityChange(product.code, e.target.value)}
                                            className="w-full p-1 border rounded text-center"
                                            disabled={product.totalStock <= 0}
                                        />
                                    </td>
                                    <td className="p-2 text-right">
                                        <button
                                            onClick={() => handleAddClick(product)}
                                            disabled={product.totalStock <= 0}
                                            className="bg-green-600 text-white px-2 py-1 rounded text-xs hover:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed"
                                        >
                                            Adicionar
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

export default ProductSearch;
