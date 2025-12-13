import React from 'react';

const Cart = ({ items, onRemoveItem, onUpdateQuantity }) => {
    const total = items.reduce((acc, item) => acc + (item.price * item.quantity), 0);

    return (
        <div className="bg-white p-4 rounded shadow">
            <h3 className="font-bold text-lg mb-2 text-dark-gray">3. Carrinho de Compras</h3>

            {items.length === 0 ? (
                <p className="text-gray-500 text-center py-4">O carrinho está vazio.</p>
            ) : (
                <>
                    <div className="overflow-x-auto mb-4">
                        <table className="w-full text-left text-sm">
                            <thead className="bg-gray-100">
                                <tr>
                                    <th className="p-2">Produto</th>
                                    <th className="p-2">Preço Unit.</th>
                                    <th className="p-2 w-24">Qtd</th>
                                    <th className="p-2">Subtotal</th>
                                    <th className="p-2 text-right">Ação</th>
                                </tr>
                            </thead>
                            <tbody>
                                {items.map((item, index) => (
                                    <tr key={`${item.code}-${index}`} className="border-t">
                                        <td className="p-2">
                                            <span className="block font-medium">{item.name}</span>
                                            <span className="text-xs text-gray-400 font-mono">{item.code}</span>
                                        </td>
                                        <td className="p-2">R$ {item.price.toFixed(2)}</td>
                                        <td className="p-2">
                                            <input
                                                type="number"
                                                min="1"
                                                value={item.quantity}
                                                onChange={(e) => onUpdateQuantity(item.code, parseInt(e.target.value))}
                                                className="w-full p-1 border rounded text-center"
                                            />
                                        </td>
                                        <td className="p-2 font-bold">
                                            R$ {(item.price * item.quantity).toFixed(2)}
                                        </td>
                                        <td className="p-2 text-right">
                                            <button
                                                onClick={() => onRemoveItem(item.code)}
                                                className="text-red-500 hover:text-red-700 font-bold text-xs"
                                            >
                                                Remover
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>

                    <div className="flex justify-between items-center bg-gray-50 p-4 rounded">
                        <span className="text-lg font-bold text-dark-gray">Total da Venda:</span>
                        <span className="text-2xl font-bold text-primary">
                            {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(total)}
                        </span>
                    </div>
                </>
            )}
        </div>
    );
};

export default Cart;
