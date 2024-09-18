import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { Button, Table } from 'react-bootstrap';
import FormularioLivros from '../components/LivrosForm';
import ConfirmarExclusaoModal from '../components/ConfirmarExclusaoModal';
import api from '../services/api';

const LivrosPage = () => {
    const [livros, setLivros] = useState([]);
    const [mostrarModal, setMostrarModal] = useState(false);
    const [dadosLivro, setDadosLivro] = useState(null);
    const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);
    const [emprestimosLivro, setEmprestimosLivro] = useState([]);

    useEffect(() => {
        fetchLivros();
    }, []);

    const fetchLivros = async () => {
        try {
            const response = await api.get('/livros');
            setLivros(response.data);
        } catch (error) {
            toast.error('Erro ao buscar livros.');
            console.error('Erro ao buscar livros:', error);
        }
    };

    const abrirModal = () => setMostrarModal(true);
    const fecharModal = () => {
        setMostrarModal(false);
        setDadosLivro(null);
    };

    const editarLivro = (livro) => {
        setDadosLivro(livro);
        setMostrarModal(true);
    };

    const verificarEmprestimosELivro = async (id) => {
        try {
            const respostaEmprestimos = await api.get(`/emprestimos/livro/verificarEmprestimo/${id}`);
            if (!respostaEmprestimos.data) {
                const listaEmprestimos = await api.get(`/emprestimos/livro/listarEmprestimo/${id}`);
                setEmprestimosLivro(listaEmprestimos.data);
                setDadosLivro({ id });
                setMostrarConfirmacao(true);
            } else {
                await api.delete(`/livros/${id}`);
                toast.success('Livro deletado com sucesso!');
                fetchLivros();
            }
        } catch (error) {
            toast.error('Erro ao verificar empréstimos ou deletar o livro.');
            console.error('Erro ao verificar empréstimos ou deletar o livro:', error);
        }
    };

    const handleConfirmDelete = async () => {
        try {
            await api.delete(`/livros/${dadosLivro.id}/excluirComEmprestimos`);
            toast.success('Livro e seus empréstimos foram deletados com sucesso!');
            setMostrarConfirmacao(false);
            fetchLivros();
        } catch (error) {
            toast.error('Erro ao excluir o livro e seus empréstimos.');
            console.error('Erro ao excluir o livro e seus empréstimos:', error);
        }
    };

    return (
        <div className="container">
            <h1>Gestão de Livros</h1>
            <Button onClick={abrirModal}>Novo Livro</Button>
            <Table striped bordered hover className="mt-3">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Autor</th>
                    <th>ISBN</th>
                    <th>Data de Publicação</th>
                    <th>Categoria</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                {livros.map((livro) => (
                    <tr key={livro.id}>
                        <td>{livro.id}</td>
                        <td>{livro.titulo}</td>
                        <td>{livro.autor}</td>
                        <td>{livro.isbn}</td>
                        <td>{livro.data_publicacao}</td>
                        <td>{livro.categoria}</td>
                        <td>
                            <Button variant="warning" onClick={() => editarLivro(livro)}>Editar</Button>{' '}
                            <Button variant="danger" onClick={() => verificarEmprestimosELivro(livro.id)}>Deletar</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <FormularioLivros estaEditando={!!dadosLivro} dadosLivro={dadosLivro} aoEnviarSucesso={() => {fecharModal();fetchLivros();}} show={mostrarModal} onHide={fecharModal}/>
            <ConfirmarExclusaoModal show={mostrarConfirmacao} onHide={() => setMostrarConfirmacao(false)} emprestimos={emprestimosLivro} onConfirm={handleConfirmDelete} tipo="livro"/>
        </div>
    );
};

export default LivrosPage;
