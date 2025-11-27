import React from 'react';

const ClientFilters = ({ searchTerm, setSearchTerm, statusFilter, setStatusFilter }) => {
    return (
        <div className="bg-white p-4 rounded-lg shadow mb-6 flex flex-wrap gap-4 items-center">
            <div className="flex-1">
                <input
                    type="text"
                    placeholder="Buscar por nome..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="w-full p-2 border border-gray-300 rounded focus:border-primary focus:outline-none"
                />
            </div>
            <div className="flex items-center gap-2">
                <label className="text-sm text-dark-gray font-medium">Status:</label>
                <select
                    value={statusFilter}
                    onChange={(e) => setStatusFilter(e.target.value === 'true')}
                    className="p-2 border border-gray-300 rounded focus:border-primary focus:outline-none"
                >
                    <option value="true">Ativos</option>
                    <option value="false">Pendentes/Inativos</option>
                </select>
            </div>
        </div>
    );
};

export default ClientFilters;
