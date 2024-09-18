import React, { useState, useEffect } from 'react';
import { format, parseISO, isValid } from 'date-fns';
import { toast } from 'react-toastify';
import { Modal, Button } from 'react-bootstrap';
import api from '../services/api';

const FormularioLivros = ({ estaEditando, dadosLivro, aoEnviarSucesso, show, onHide }) => {
    const [livro, setLivro] = useState({
        id: '',
        titulo: '',
        autor: '',
        isbn: '',
        data_publicacao: '',
        categoria: '',
    });

    useEffect(() => {
        if (estaEditando && dadosLivro) {
            setLivro({
                id: dadosLivro.id || '',
                titulo: dadosLivro.titulo || '',
                autor: dadosLivro.autor || '',
                isbn: dadosLivro.isbn || '',
                data_publicacao: dadosLivro.data_publicacao && isValid(parseISO(dadosLivro.data_publicacao))
                    ? format(parseISO(dadosLivro.data_publicacao), 'yyyy-MM-dd')
                    : '',
                categoria: dadosLivro.categoria || '',
            });
        } else {
            setLivro({
                id: '',
                titulo: '',
                autor: '',
                isbn: '',
                data_publicacao: '',
                categoria: '',
            });
        }
    }, [estaEditando, dadosLivro, show]);

    const aoMudar = (e) => {
        setLivro({ ...livro, [e.target.name]: e.target.value });
    };

    const aoEnviar = async (e) => {
        e.preventDefault();

        if (!livro.titulo || !livro.autor || !livro.isbn || !livro.data_publicacao || !livro.categoria) {
            toast.error('Todos os campos devem estar preenchidos.');
            return;
        }

        const dataPublicacao = new Date(livro.data_publicacao);
        const dataAtual = new Date();

        if (dataPublicacao > dataAtual) {
            toast.error('A data de publicação não pode ser no futuro.');
            return;
        }

        const livroFormatado = {
            ...livro,
            data_publicacao: format(new Date(livro.data_publicacao), 'dd/MM/yyyy'),
        };

        try {
            if (estaEditando) {
                await api.put(`/livros`, livroFormatado);
                toast.success('Livro atualizado com sucesso!');
            } else {
                await api.post('/livros', livroFormatado);
                toast.success('Livro salvo com sucesso!');
            }

            if (typeof aoEnviarSucesso === 'function') {
                aoEnviarSucesso();
            }

            setLivro({
                id: '',
                titulo: '',
                autor: '',
                isbn: '',
                data_publicacao: '',
                categoria: '',
            });

            onHide();
        } catch (erro) {
            toast.error('Erro ao salvar o livro.');
            console.error('Erro ao salvar o livro:', erro);
        }
    };

    const handleClose = () => {
        setLivro({
            id: '',
            titulo: '',
            autor: '',
            isbn: '',
            data_publicacao: '',
            categoria: '',
        });
        onHide();
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>{estaEditando ? 'Editar Livro' : 'Novo Livro'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form onSubmit={aoEnviar}>
                    <input type="hidden" name="id" value={livro.id} />
                    <label className="form-label">Título</label>
                    <input className="form-control" name="titulo" value={livro.titulo} onChange={aoMudar} required/>
                    <label className="form-label">Autor</label>
                    <input className="form-control" name="autor" value={livro.autor} onChange={aoMudar} required/>

                    <label className="form-label">ISBN</label>
                    <input className="form-control" name="isbn" value={livro.isbn} onChange={aoMudar} required/>

                    <label className="form-label">Data de Publicação</label>
                    <input className="form-control" type="date" name="data_publicacao" value={livro.data_publicacao} onChange={aoMudar} required/>

                    <label className="form-label">Categoria</label>
                    <input className="form-control" name="categoria" value={livro.categoria} onChange={aoMudar} required/>
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

export default FormularioLivros;
