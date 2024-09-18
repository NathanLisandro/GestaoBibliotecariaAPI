import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { Button, Table } from 'react-bootstrap';
import FormularioUsuario from '../components/UsuarioForm';
import ConfirmarExclusaoModal from '../components/ConfirmarExclusaoModal';
import api from '../services/api';

const UsuariosPage = () => {
    const [usuarios, setUsuarios] = useState([]);
    const [mostrarModal, setMostrarModal] = useState(false);
    const [dadosUsuario, setDadosUsuario] = useState(null);
    const [mostrarConfirmacao, setMostrarConfirmacao] = useState(false);
    const [emprestimosUsuario, setEmprestimosUsuario] = useState([]);

    useEffect(() => {
        fetchUsuarios();
    }, []);

    const fetchUsuarios = async () => {
        try {
            const response = await api.get('/usuarios');
            setUsuarios(response.data);
        } catch (error) {
            toast.error('Erro ao buscar usuários.');
            console.error('Erro ao buscar usuários:', error);
        }
    };

    const handleShow = () => setMostrarModal(true);
    const handleClose = () => {
        setMostrarModal(false);
        setDadosUsuario(null);
    };

    const handleEdit = (usuario) => {
        setDadosUsuario(usuario);
        setMostrarModal(true);
    };

    const handleDelete = async (id) => {
        try {
            await api.delete(`/usuarios/${id}`);
            toast.success('Usuário deletado com sucesso!');
            fetchUsuarios();
        } catch (error) {
            if (error.response && error.response.status === 409) {
                toast.warn('Este usuário possui empréstimos. Verificando empréstimos...');

                const respostaEmprestimos = await api.get(`/emprestimos/usuario/${id}`);
                setEmprestimosUsuario(respostaEmprestimos.data);
                setDadosUsuario({ id });
                setMostrarConfirmacao(true);
            } else {
                toast.error('Erro ao deletar o usuário.');
                console.error('Erro ao deletar o usuário:', error);
            }
        }
    };

    const handleConfirmDelete = async () => {
        try {
            await api.delete(`/usuarios/${dadosUsuario.id}/excluirComEmprestimos`);
            toast.success('Usuário e seus empréstimos foram deletados com sucesso!');
            setMostrarConfirmacao(false);
            fetchUsuarios();
        } catch (error) {
            toast.error('Erro ao excluir o usuário e seus empréstimos.');
            console.error('Erro ao excluir o usuário e seus empréstimos:', error);
        }
    };

    return (
        <div className="container">
            <h1>Gestão de Usuários</h1>
            <Button onClick={handleShow}>Novo Usuário</Button>
            <Table striped bordered hover className="mt-3">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Email</th>
                    <th>Data de Cadastro</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
                </thead>
                <tbody>
                {usuarios.map((usuario) => (
                    <tr key={usuario.id}>
                        <td>{usuario.id}</td>
                        <td>{usuario.nome}</td>
                        <td>{usuario.email}</td>
                        <td>{usuario.dataCadastro}</td>
                        <td>{usuario.telefone}</td>
                        <td>
                            <Button variant="warning" onClick={() => handleEdit(usuario)}>Editar</Button>{' '}
                            <Button variant="danger" onClick={() => handleDelete(usuario.id)}>Deletar</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </Table>
            <FormularioUsuario estaEditando={!!dadosUsuario} dadosUsuario={dadosUsuario} aoEnviarSucesso={() => {handleClose(); fetchUsuarios();}} show={mostrarModal} onHide={handleClose}/>
            <ConfirmarExclusaoModal show={mostrarConfirmacao} onHide={() => setMostrarConfirmacao(false)} emprestimos={emprestimosUsuario} onConfirm={handleConfirmDelete}/>
        </div>
    );
};

export default UsuariosPage;
