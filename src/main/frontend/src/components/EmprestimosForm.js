import React, { useState, useEffect } from 'react';
import { format, parseISO, isValid } from 'date-fns';
import { toast } from 'react-toastify';
import { Modal, Button, Form } from 'react-bootstrap';
import api from '../services/api';

const FormularioEmprestimos = ({ estaEditando, dadosEmprestimo, aoEnviarSucesso, show, onHide }) => {
    const resetEmprestimo = () => ({
        id: '',
        usuarioId: '',
        livroId: '',
        dataEmprestimo: '',
        dataDevolucao: '',
        status: ''
    });
    const [emprestimo, setEmprestimo] = useState(resetEmprestimo());
    const [usuarios, setUsuarios] = useState([]);
    const [livros, setLivros] = useState([]);

    useEffect(() => {
        if (estaEditando && dadosEmprestimo) {
            setEmprestimo(mapearDadosEmprestimo(dadosEmprestimo));
        } else {
            setEmprestimo(resetEmprestimo());
        }
    }, [estaEditando, dadosEmprestimo, show]);

    useEffect(() => {
        fetchUsuarios();
        fetchLivros();
    }, []);

    const fetchUsuarios = async () => {
        try {
            const response = await api.get('/usuarios');
            setUsuarios(response.data);
        } catch (error) {
            toast.error('Erro ao buscar usuários.');
        }
    };

    const fetchLivros = async () => {
        try {
            const response = await api.get('/livros');
            setLivros(response.data);
        } catch (error) {
            toast.error('Erro ao buscar livros.');
        }
    };


    const mapearDadosEmprestimo = (dados) => ({
        id: dados.id || '',
        usuarioId: dados.usuario.id || '',
        livroId: dados.livro.id || '',
        dataEmprestimo: dados.dataEmprestimo && isValid(parseISO(dados.dataEmprestimo)) ? format(parseISO(dados.dataEmprestimo), 'yyyy-MM-dd') : '',
        dataDevolucao: dados.dataDevolucao && isValid(parseISO(dados.dataDevolucao)) ? format(parseISO(dados.dataDevolucao), 'yyyy-MM-dd') : '',
        status: dados.status || '',
    });

    const aoMudar = (e) => {
        setEmprestimo({ ...emprestimo, [e.target.name]: e.target.value });
    };

    const verificarEmprestimoAtivo = async (livroId) => {
        try {
            const response = await api.get(`/emprestimos/livro/verificarEmprestimo/${livroId}`);
            return !response.data;
        } catch (error) {
            toast.error('Erro ao verificar o status do livro.');
            return false;
        }
    };


    const aoEnviar = async (e) => {
        e.preventDefault();
        if (!validarFormulario()) return;

        try {
            const livroEmprestado = await verificarEmprestimoAtivo(emprestimo.livroId);
            if (livroEmprestado) {
                toast.error('Este livro já está emprestado. Não pode ser emprestado novamente até que seja devolvido.');
                return;
            }

            const emprestimoFormatado = formatarEmprestimo(emprestimo);

            if (estaEditando) {
                await api.put(`/emprestimos`, emprestimoFormatado);
                toast.success('Empréstimo atualizado com sucesso!');
            } else {
                await api.post('/emprestimos', emprestimoFormatado);
                toast.success('Empréstimo salvo com sucesso!');
            }

            if (typeof aoEnviarSucesso === 'function') {
                aoEnviarSucesso();
            }
            setEmprestimo(resetEmprestimo());
            onHide();
        } catch (erro) {
            toast.error('Erro ao salvar o empréstimo.');
        }
    };


    const validarFormulario = () => {
        if (!emprestimo.usuarioId || !emprestimo.livroId || !emprestimo.dataDevolucao || !emprestimo.status) {
            toast.error('Todos os campos devem estar preenchidos.');
            return false;
        }

        const dataEmprestimo = new Date(emprestimo.dataEmprestimo);
        const dataDevolucao = new Date(emprestimo.dataDevolucao);

        if (dataDevolucao < dataEmprestimo) {
            toast.error('A data de devolução não pode ser menor que a data de empréstimo. Por favor, insira uma data válida.');
            return false;
        }

        return true;
    };

    const formatarEmprestimo = (emprestimo) => ({
        ...emprestimo,
        dataEmprestimo: emprestimo.dataEmprestimo ? format(new Date(emprestimo.dataEmprestimo), 'dd/MM/yyyy') : '',
        dataDevolucao: format(new Date(emprestimo.dataDevolucao), 'dd/MM/yyyy'),
    });

    const handleClose = () => {
        setEmprestimo(resetEmprestimo());
        onHide();
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>{estaEditando ? 'Editar Empréstimo' : 'Novo Empréstimo'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form onSubmit={aoEnviar}>
                    <input type="hidden" name="id" value={emprestimo.id} />
                    <label className="form-label">Usuário</label>
                    <Form.Select name="usuarioId" value={emprestimo.usuarioId} onChange={aoMudar} required disabled={estaEditando}>
                        <option value="">Selecione um usuário</option>
                        {usuarios.map(usuario => (
                            <option key={usuario.id} value={usuario.id}>
                                {usuario.nome}
                            </option>
                        ))}
                    </Form.Select>

                    <label className="form-label">Livro</label>
                    <Form.Select name="livroId" value={emprestimo.livroId} onChange={aoMudar} required disabled={estaEditando}>
                        <option value="">Selecione um livro</option>
                        {livros.map(livro => (
                            <option key={livro.id} value={livro.id}>
                                {livro.titulo}
                            </option>
                        ))}
                    </Form.Select>

                    {!estaEditando && (
                        <>
                            <label className="form-label">Data de Empréstimo</label>
                            <input className="form-control" type="date" name="dataEmprestimo" value={emprestimo.dataEmprestimo} onChange={aoMudar} required/>
                        </>
                    )}

                    <label className="form-label">Data de Devolução</label>
                    <input className="form-control" type="date" name="dataDevolucao" value={emprestimo.dataDevolucao} onChange={aoMudar} required/>

                    <label className="form-label">Status</label>
                    <input className="form-control" name="status" value={emprestimo.status} onChange={aoMudar} required/>
                </form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Cancelar
                </Button>
                <Button variant="primary" type="submit" onClick={aoEnviar}>
                    {estaEditando ? 'Atualizar' : 'Salvar'}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default FormularioEmprestimos;
