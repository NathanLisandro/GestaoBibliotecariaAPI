import React, {useState, useEffect} from 'react';
import {format, parseISO, isValid} from 'date-fns';
import {toast} from 'react-toastify';
import {Modal, Button} from 'react-bootstrap';
import api from '../services/api';

const FormularioUsuario = ({estaEditando, dadosUsuario, aoEnviarSucesso, show, onHide}) => {
    const [usuario, setUsuario] = useState({
        id: '',
        nome: '',
        email: '',
        dataCadastro: '',
        telefone: '',
    });

    useEffect(() => {
        if (estaEditando && dadosUsuario) {
            setUsuario({
                id: dadosUsuario.id || '',
                nome: dadosUsuario.nome || '',
                email: dadosUsuario.email || '',
                dataCadastro: dadosUsuario.dataCadastro && isValid(parseISO(dadosUsuario.dataCadastro)) ? format(parseISO(dadosUsuario.dataCadastro), 'yyyy-MM-dd') : '',
                telefone: dadosUsuario.telefone || '',
            });
        } else {
            setUsuario({
                id: '',
                nome: '',
                email: '',
                dataCadastro: '',
                telefone: '',
            });
        }
    }, [estaEditando, dadosUsuario, show]);

    const aoMudar = (e) => {
        setUsuario({...usuario, [e.target.name]: e.target.value});
    };

    const aoEnviar = async (e) => {
        e.preventDefault();

        if (!usuario.nome || !usuario.email || !usuario.dataCadastro || !usuario.telefone) {
            toast.error('Todos os campos devem estar preenchidos.');
            return;
        }

        const usuarioFormatado = {
            ...usuario,
            dataCadastro: format(new Date(usuario.dataCadastro), 'dd/MM/yyyy'),
        };

        try {
            if (estaEditando) {
                await api.put(`/usuarios`, usuarioFormatado);
                toast.success('Usuário atualizado com sucesso!');
            } else {
                await api.post('/usuarios', usuarioFormatado);
                toast.success('Usuário salvo com sucesso!');
            }

            if (typeof aoEnviarSucesso === 'function') {
                aoEnviarSucesso();
            }

            setUsuario({
                id: '',
                nome: '',
                email: '',
                dataCadastro: '',
                telefone: '',
            });

            onHide();
        } catch (erro) {
            toast.error('Erro ao salvar o usuário.');
            console.error('Erro ao salvar o usuário:', erro);
        }
    };

    const handleClose = () => {
        setUsuario({
            id: '',
            nome: '',
            email: '',
            dataCadastro: '',
            telefone: '',
        });
        onHide();
    };

    return (
        <Modal show={show} onHide={handleClose}>
            <Modal.Header closeButton>
                <Modal.Title>{estaEditando ? 'Editar Usuário' : 'Novo Usuário'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form onSubmit={aoEnviar}>
                    <input type="hidden" name="id" value={usuario.id}/>

                    <label className="form-label">Nome</label>
                    <input className="form-control" name="nome" value={usuario.nome} onChange={aoMudar} required/>

                    <label className="form-label">Email</label>
                    <input className="form-control" type="email" name="email" value={usuario.email} onChange={aoMudar} required/>

                    <label className="form-label">Data de Cadastro</label>
                    <input className="form-control" type="date" name="dataCadastro" value={usuario.dataCadastro} onChange={aoMudar} required/>

                    <label className="form-label">Telefone</label>
                    <input className="form-control" name="telefone" value={usuario.telefone} onChange={aoMudar} required/>
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

export default FormularioUsuario;
