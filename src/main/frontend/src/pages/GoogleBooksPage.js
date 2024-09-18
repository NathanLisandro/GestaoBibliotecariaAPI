import React, { useState } from 'react';
import { toast } from 'react-toastify';
import api from '../services/api';

const GoogleBooksPage = () => {
    const [titulo, setTitulo] = useState('');
    const [livros, setLivros] = useState([]);

    const handleSubmit = (e) => {
        e.preventDefault();
        api.get(`/googlebooks/buscar?titulo=${titulo}`)
            .then((response) => setLivros(response.data))
            .catch((error) => {
                console.error('Erro ao buscar livros:', error);
                toast.error('Erro ao buscar livros. Tente novamente.');
            });
    };

    const adicionarLivro = (livro) => {
        api.post('/googlebooks/adicionar', livro)
            .then(() => toast.success('Livro adicionado com sucesso!'))
            .catch((error) => {
                if (error.response && error.response.status === 409) {
                    const mensagemErro = error.response.data.mensagem || error.response.data.message;
                    if (mensagemErro === "ISBN já existe" || mensagemErro.includes("ISBN já existe")) {
                        return toast.error('Este livro já está cadastrado, não é possível adicionar');
                    }
                }
                toast.error('Ocorreu um erro interno no servidor, por favor tente novamente mais tarde');
            });
    };


    return (
        <div className="container">
            <h1>Buscar Livros no Google Books</h1>
            <form onSubmit={handleSubmit}>
                <label className="form-label">Título do Livro</label>
                <input className="form-control" type="text" value={titulo} onChange={(e) => setTitulo(e.target.value)} required/>
                <button className="button" type="submit">Buscar</button>
            </form>
            {livros.length > 0 && (
                <div>
                    <h2>Resultados da Busca:</h2>
                    <ul>
                        {livros.map((livro, index) => (
                            <li key={index}>
                                {livro.volumeInfo.title} - {livro.volumeInfo.authors?.join(', ')}
                                <button className="button" onClick={() => adicionarLivro(livro)}>Adicionar</button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default GoogleBooksPage;
