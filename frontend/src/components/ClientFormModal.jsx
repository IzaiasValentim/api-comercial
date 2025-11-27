import React from 'react';

const ClientFormModal = ({ showModal, setShowModal, isEditing, formData, handleInputChange, handleSubmit }) => {
    if (!showModal) return null;

    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 backdrop-blur-sm">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl p-6 m-4 overflow-y-auto max-h-[90vh]">
                <div className="flex justify-between items-center mb-4">
                    <h3 className="text-xl font-bold text-primary">
                        {isEditing ? 'Editar Cliente' : 'Novo Cliente'}
                    </h3>
                    <button onClick={() => setShowModal(false)} className="text-gray-400 hover:text-gray-600">✕</button>
                </div>

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div><label className="block text-sm font-medium mb-1">Nome Completo</label><input name="name" value={formData.name} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                        <div>
                            <label className="block text-sm font-medium mb-1">CPF / CNPJ</label>
                            <input
                                name="identificationNumber"
                                value={formData.identificationNumber}
                                onChange={handleInputChange}
                                required
                                disabled={isEditing} // CPF é a chave, não deve mudar na edição
                                className={`w-full p-2 border rounded ${isEditing ? 'bg-gray-100 cursor-not-allowed' : ''}`}
                            />
                        </div>
                    </div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div><label className="block text-sm font-medium mb-1">Email</label><input type="email" name="email" value={formData.email} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                        <div><label className="block text-sm font-medium mb-1">Forma de Pagamento</label>
                            <select name="payment" value={formData.payment} onChange={handleInputChange} className="w-full p-2 border rounded">
                                <option value="Dinheiro">Dinheiro</option>
                                <option value="Cartao_de_Credito">Cartão de Crédito</option>
                                <option value="Cartao_de_Debito">Cartão de Débito</option>
                                <option value="Pix">Pix</option>
                                <option value="Boleto">Boleto</option>
                            </select>
                        </div>
                    </div>

                    <div><label className="block text-sm font-medium mb-1">Endereço</label><input name="address" value={formData.address} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div><label className="block text-sm font-medium mb-1">Telefone</label><input name="phoneNumber" value={formData.phoneNumber} onChange={handleInputChange} className="w-full p-2 border rounded" /></div>
                        <div><label className="block text-sm font-medium mb-1">Telefone Reserva</label><input name="phoneNumberReserve" value={formData.phoneNumberReserve} onChange={handleInputChange} className="w-full p-2 border rounded" /></div>
                    </div>

                    <div className="flex justify-end gap-3 mt-6">
                        <button type="button" onClick={() => setShowModal(false)} className="px-4 py-2 text-dark-gray hover:bg-gray-100 rounded">Cancelar</button>
                        <button type="submit" className="px-4 py-2 bg-primary text-white rounded hover:bg-primary-hover">
                            {isEditing ? 'Salvar Alterações' : 'Cadastrar'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default ClientFormModal;
