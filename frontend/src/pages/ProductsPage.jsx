import React, { useEffect, useState, useCallback } from 'react';
import api from '../services/api';
import { Link } from 'react-router-dom';

export const ProductsPage = () => {
  // --- ESTADOS GERAIS ---
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [page, setPage] = useState(0);
  const pageSize = 10;

  // --- ESTADOS DOS MODAIS ---
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showStockModal, setShowStockModal] = useState(false);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  // --- ESTADOS DE DADOS ---
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [productDetails, setProductDetails] = useState(null);
  
  // Estado único para formulários (Create e Add Stock)
  const [formData, setFormData] = useState({
    name: '',
    type: '',
    price: '',
    quantity: '',
    code: '',
    hasValidity: false,
    validity: ''
  });

  // --- BUSCAR PRODUTOS AGREGADOS (LISTA PRINCIPAL) ---
  const fetchProducts = useCallback(async () => {
    try {
      setLoading(true);
      const response = await api.get(`/api/items/getAllAgregated?page=${page}&quantidade=${pageSize}`);
      setProducts(response.data);
    } catch (err) {
      console.error(err);
      setError('Erro ao carregar produtos.');
    } finally {
      setLoading(false);
    }
  }, [page]);

  useEffect(() => {
    fetchProducts();
  }, [fetchProducts]);

  // --- FUNÇÕES DE FORMULÁRIO ---
  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  // Resetar form ao fechar modal
  const resetForm = () => {
    setFormData({
      name: '', type: '', price: '', quantity: '', code: '', hasValidity: false, validity: ''
    });
    setSelectedProduct(null);
  };

  // --- AÇÃO: CRIAR NOVO PRODUTO ---
  const handleCreateSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        price: parseFloat(formData.price),
        quantity: parseFloat(formData.quantity),
        validity: formData.hasValidity && formData.validity ? new Date(formData.validity).toISOString() : null
      };

      await api.post('/api/items/', payload);
      alert('Produto criado com sucesso!');
      setShowCreateModal(false);
      resetForm();
      fetchProducts();
    } catch (err) {
      console.error(err);
      alert('Erro ao criar produto. Verifique se o código/nome já existe.');
    }
  };

  // --- AÇÃO: PREPARAR ADIÇÃO DE ESTOQUE ---
  const openStockModal = (product) => {
    setSelectedProduct(product);
    // Preenche o formulário com os dados conhecidos
    setFormData({
      name: product.nome,
      type: '', // O backend busca o tipo automaticamente pelo nome
      price: product.price, // Sugere o preço atual
      quantity: '',
      code: product.code, // Apenas visual
      hasValidity: false,
      validity: ''
    });
    setShowStockModal(true);
  };

  // --- AÇÃO: ENVIAR NOVO ESTOQUE ---
  const handleStockSubmit = async (e) => {
    e.preventDefault();
    try {
      
      // Endpoint específico para adicionar estoque (/addStockByName/)
      const payload = {
        code: formData.code,
        price: parseFloat(formData.price),
        quantity: parseFloat(formData.quantity),
        validity: formData.hasValidity && formData.validity ? new Date(formData.validity).toISOString() : null,
        hasValidity: formData.hasValidity,
      };
console.log(payload.code)
      await api.post('/api/items/addStockByCode/', payload);
      alert('Estoque adicionado com sucesso!');
      setShowStockModal(false);
      resetForm();
      fetchProducts();
    } catch (err) {
      console.error(err);
      alert('Erro ao adicionar estoque.'+ err.message);
    }
  };

  // --- AÇÃO: VER DETALHES (LOTES) ---
  const openDetailsModal = async (code) => {
    try {
      const response = await api.get(`/api/items/itemStockByCode?code=${code}`);
      setProductDetails(response.data); // Retorna ItemStockDTO com lista de 'itens' (lotes)
      setShowDetailsModal(true);
    } catch (err) {
      alert('Erro ao carregar detalhes do produto.' + err.message);
    }
  };

  // --- AÇÃO: EXCLUIR LOTE ---
  const handleDeleteBatch = async (batch) => {
    if (window.confirm(`Tem certeza que deseja excluir o lote ${batch}? Isso removerá o estoque associado.`)) {
      try {
        await api.delete(`/api/items/deleteByBatch?batch=${batch}`);
        alert('Lote removido!');
        // Recarrega os detalhes para atualizar a lista de lotes
        if (productDetails) {
             openDetailsModal(productDetails.code);
        }
        // Recarrega a lista principal para atualizar o estoque total
        fetchProducts();
      } catch (err) {
        console.error(err);
        alert('Erro ao excluir lote.');
      }
    }
  };

  return (
    <div className="flex h-screen bg-light-gray">
      {/* Sidebar */}
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
            <h2 className="text-3xl font-bold text-dark-gray">Produtos</h2>
            <button 
                onClick={() => setShowCreateModal(true)}
                className="bg-primary hover:bg-primary-hover text-white font-bold py-2 px-4 rounded transition-colors"
            >
                + Novo Produto
            </button>
        </div>

        {/* LISTA PRINCIPAL */}
        {loading ? (
          <p className="text-medium-gray">Carregando...</p>
        ) : (
          <div className="overflow-x-auto rounded-lg bg-white shadow">
            <table className="w-full text-left border-collapse">
              <thead>
                <tr className="bg-secondary text-primary">
                  <th className="p-4 font-semibold">Cód.</th>
                  <th className="p-4 font-semibold">Nome</th>
                  <th className="p-4 font-semibold">Preço Atual</th>
                  <th className="p-4 font-semibold">Estoque Total</th>
                  <th className="p-4 font-semibold text-center">Ações</th>
                </tr>
              </thead>
              <tbody>
                {products.length > 0 ? products.map((product) => (
                  <tr key={product.id} className="border-b border-light-gray hover:bg-gray-50">
                    <td className="p-4 text-medium-gray font-mono text-sm">{product.code}</td>
                    <td className="p-4 font-medium text-dark-gray">{product.name}</td>
                    <td className="p-4 text-dark-gray">
                      {new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(product.price)}
                    </td>
                    <td className="p-4">
                        <span className={`px-2 py-1 rounded text-xs font-bold ${product.totalStock > 0 ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                            {product.totalStock}
                        </span>
                    </td>
                    <td className="p-4 text-center flex justify-center gap-2">
                      <button 
                        onClick={() => openStockModal(product)}
                        className="text-green-600 hover:text-green-800 font-medium text-sm border border-green-200 px-2 py-1 rounded hover:bg-green-50"
                        title="Adicionar Estoque (Novo Lote)"
                      >
                        + Estoque
                      </button>
                      <button 
                        onClick={() => openDetailsModal(product.code)}
                        className="text-primary hover:text-primary-hover font-medium text-sm border border-blue-200 px-2 py-1 rounded hover:bg-blue-50"
                        title="Ver Lotes e Excluir"
                      >
                        Detalhes
                      </button>
                    </td>
                  </tr>
                )) : (
                    <tr><td colSpan="5" className="p-8 text-center text-medium-gray">Nenhum produto encontrado.</td></tr>
                )}
              </tbody>
            </table>
             {/* Paginação Simples */}
             <div className="p-4 border-t border-gray-200 flex justify-end gap-2">
                <button disabled={page === 0} onClick={() => setPage(page - 1)} className="px-3 py-1 rounded border hover:bg-gray-100 disabled:opacity-50">Ant</button>
                <span className="px-3 py-1">Pág {page + 1}</span>
                <button onClick={() => setPage(page + 1)} className="px-3 py-1 rounded border hover:bg-gray-100">Prox</button>
            </div>
          </div>
        )}

        {/* --- MODAL 1: CRIAR NOVO PRODUTO --- */}
        {showCreateModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 backdrop-blur-sm">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-lg p-6 m-4">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold text-primary">Novo Produto</h3>
                <button onClick={() => setShowCreateModal(false)} className="text-gray-400 hover:text-gray-600">✕</button>
              </div>
              <form onSubmit={handleCreateSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <div><label className="block text-sm font-medium mb-1">Nome</label><input name="name" value={formData.name} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                    <div><label className="block text-sm font-medium mb-1">Código</label><input name="code" value={formData.code} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                </div>
                <div className="grid grid-cols-2 gap-4">
                    <div><label className="block text-sm font-medium mb-1">Tipo</label><input name="type" value={formData.type} onChange={handleInputChange} className="w-full p-2 border rounded" /></div>
                    <div><label className="block text-sm font-medium mb-1">Preço (R$)</label><input type="number" step="0.01" name="price" value={formData.price} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                </div>
                <div><label className="block text-sm font-medium mb-1">Qtd Inicial</label><input type="number" name="quantity" value={formData.quantity} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                <div className="flex items-center gap-2"><input type="checkbox" id="hasValid1" name="hasValidity" checked={formData.hasValidity} onChange={handleInputChange} /><label htmlFor="hasValid1">Tem validade?</label></div>
                {formData.hasValidity && (<div><label className="block text-sm font-medium mb-1">Data</label><input type="date" name="validity" value={formData.validity} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>)}
                <div className="flex justify-end gap-3 mt-6">
                    <button type="button" onClick={() => setShowCreateModal(false)} className="px-4 py-2 text-dark-gray hover:bg-gray-100 rounded">Cancelar</button>
                    <button type="submit" className="px-4 py-2 bg-primary text-white rounded hover:bg-primary-hover">Salvar</button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* --- MODAL 2: ADICIONAR ESTOQUE (NOVO LOTE) --- */}
        {showStockModal && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 backdrop-blur-sm">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-lg p-6 m-4">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold text-primary">Adicionar Estoque</h3>
                <button onClick={() => setShowStockModal(false)} className="text-gray-400 hover:text-gray-600">✕</button>
              </div>
              <p className="mb-4 text-sm text-medium-gray">Adicionando novo lote para: <strong>{formData.name}</strong></p>
              
              <form onSubmit={handleStockSubmit} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                    <div><label className="block text-sm font-medium mb-1">Preço (R$)</label><input type="number" step="0.01" name="price" value={formData.price} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                    <div><label className="block text-sm font-medium mb-1">Quantidade</label><input type="number" name="quantity" value={formData.quantity} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>
                </div>
                <div className="flex items-center gap-2"><input type="checkbox" id="hasValid2" name="hasValidity" checked={formData.hasValidity} onChange={handleInputChange} /><label htmlFor="hasValid2">Tem validade?</label></div>
                {formData.hasValidity && (<div><label className="block text-sm font-medium mb-1">Data Validade</label><input type="date" name="validity" value={formData.validity} onChange={handleInputChange} required className="w-full p-2 border rounded" /></div>)}
                
                <div className="flex justify-end gap-3 mt-6">
                    <button type="button" onClick={() => setShowStockModal(false)} className="px-4 py-2 text-dark-gray hover:bg-gray-100 rounded">Cancelar</button>
                    <button type="submit" className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700">Adicionar</button>
                </div>
              </form>
            </div>
          </div>
        )}

        {/* --- MODAL 3: DETALHES (VER LOTES / EXCLUIR) --- */}
        {showDetailsModal && productDetails && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 backdrop-blur-sm">
            <div className="bg-white rounded-lg shadow-xl w-full max-w-2xl p-6 m-4">
              <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold text-primary">Detalhes: {productDetails.name}</h3>
                <button onClick={() => setShowDetailsModal(false)} className="text-gray-400 hover:text-gray-600">✕</button>
              </div>
              
              <div className="mb-4 grid grid-cols-3 gap-4 bg-secondary p-3 rounded">
                <div><span className="block text-xs text-medium-gray">Código Principal</span><span className="font-mono font-bold">{productDetails.code}</span></div>
                <div><span className="block text-xs text-medium-gray">Estoque Total</span><span className="font-bold">{productDetails.totalStock}</span></div>
                <div><span className="block text-xs text-medium-gray">Preço Ref.</span><span className="font-bold">R$ {productDetails.price}</span></div>
              </div>

              <h4 className="font-bold text-dark-gray mb-2">Lotes Disponíveis</h4>
              <div className="max-h-60 overflow-y-auto border rounded">
                <table className="w-full text-left text-sm">
                  <thead className="bg-gray-100 text-dark-gray sticky top-0">
                    <tr>
                      <th className="p-2">Lote (Batch)</th>
                      <th className="p-2">Qtd</th>
                      <th className="p-2">Preço</th>
                      <th className="p-2">Validade</th>
                      <th className="p-2 text-right">Ação</th>
                    </tr>
                  </thead>
                  <tbody>
                    {productDetails.batches && productDetails.batches.length > 0 ? productDetails.batches.map((batch_item) => (
                      <tr key={batch_item.batch || Math.random()} className="border-t">
                         <td className="p-2 font-mono text-xs">{batch_item.batch}</td>
                         <td className="p-2">{batch_item.quantity}</td>
                         <td className="p-2">R$ {batch_item.price}</td>
                         <td className="p-2">{batch_item.validity ? new Date(batch_item.validity).toLocaleDateString() : '-'}</td>
                         <td className="p-2 text-right">
                           <button 
                             onClick={() => handleDeleteBatch(batch_item.batch)}
                             className="text-red-500 hover:text-red-700 hover:underline text-xs font-bold"
                           >
                             Excluir
                           </button>
                         </td>
                      </tr>
                    )) : (
                      <tr><td colSpan="5" className="p-4 text-center text-gray-500">Nenhum lote encontrado.</td></tr>
                    )}
                  </tbody>
                </table>
              </div>

              <div className="flex justify-end mt-6">
                 <button onClick={() => setShowDetailsModal(false)} className="px-4 py-2 bg-gray-200 hover:bg-gray-300 rounded text-dark-gray">Fechar</button>
              </div>
            </div>
          </div>
        )}

      </main>
    </div>
  );
};