import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import api from '../services/api';

const RecomendacoesPage = () => {
    const [usuarios, setUsuarios] = useState([]);
    const [usuarioId, setUsuarioId] = useState('');
    const [usuarioNome, setUsuarioNome] = useState('');
    const [livros, setLivros] = useState([]);

    useEffect(() => {
        const fetchUsuarios = async () => {
            try {
                const response = await api.get('/usuarios');
                setUsuarios(response.data);
            } catch (error) {
                toast.error('Erro ao buscar usuários.');
                console.error('Erro ao buscar usuários:', error);
            }
        };
        fetchUsuarios();
    }, []);

    const handleSelectChange = (e) => {
        const selectedUser = usuarios.find(user => user.id === parseInt(e.target.value, 10));
        setUsuarioId(selectedUser?.id || '');
        setUsuarioNome(selectedUser?.nome || '');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const recomendacoesResponse = await api.get(`/livros/recomendacoes/${usuarioId}`);
            setLivros(recomendacoesResponse.data);
            toast.success('Recomendações obtidas com sucesso!');
        } catch (error) {
            toast.error('Erro ao buscar recomendações.');
            console.error(error);
        }
    };

    return (
        <div className="container">
            <h1>Recomendações de Livros</h1>
            <form onSubmit={handleSubmit}>
                <label className="form-label">Selecione o Usuário</label>
                <select className="form-control" value={usuarioId} onChange={handleSelectChange} required>
                    <option value="">Selecione um usuário</option>
                    {usuarios.map((usuario) => (
                        <option key={usuario.id} value={usuario.id}>
                            {usuario.nome}
                        </option>
                    ))}
                </select>
                <button className="button" type="submit">Obter Recomendações</button>
            </form>

            {usuarioNome && livros.length > 0 && (
                <div>
                    <h2>Recomendações para {usuarioNome}:</h2>
                    <table className="table">
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Título</th>
                            <th>Autor</th>
                        </tr>
                        </thead>
                        <tbody>
                        {livros.map((livro) => (
                            <tr key={livro.id}>
                                <td>{livro.id}</td>
                                <td>{livro.titulo}</td>
                                <td>{livro.autor}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default RecomendacoesPage;
