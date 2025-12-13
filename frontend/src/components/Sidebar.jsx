import React from 'react';
import { Link } from 'react-router-dom';

const Sidebar = () => {
    return (
        <aside className="w-64 bg-white shadow-md hidden md:block">
            <div className="p-6">
                <h1 className="text-2xl font-bold text-primary">ClickComercial</h1>
            </div>
            <nav className="mt-6 px-4 space-y-2">
                <Link to="/dashboard" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Dashboard</Link>
                <Link to="/users" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Usuários</Link>
                <Link to="/products" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Produtos</Link>
                <Link to="/clients" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Clientes</Link>
                <Link to="/sales" className="block rounded-md px-4 py-2 text-dark-gray hover:bg-gray-100">Vendas</Link>
            </nav>
        </aside>
    );
};

export default Sidebar;
