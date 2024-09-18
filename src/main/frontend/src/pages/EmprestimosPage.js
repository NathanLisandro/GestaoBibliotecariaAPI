import React, { useState, useEffect } from 'react';
import { toast } from 'react-toastify';
import { Button, Table } from 'react-bootstrap';
import { format, parseISO, isValid } from 'date-fns';
import FormularioEmprestimos from '../components/EmprestimosForm';
import api from '../services/api';

const EmprestimosPage = () => {
    const [emprestimos, setEmprestimos] = useState([]);
    const [mostrarModal, setMostrarModal] = useState(false);
    const [dadosEmprestimo, setDadosEmprestimo] = useState(null);

    useEffect(() => {
        buscarEmprestimos();
    }, []);

    const buscarEmprestimos = async () => {
        try {
            const resposta = await api.get('/emprestimos?page=0&size=10');
            console.log('Dados recebidos:', resposta.data.content);

            if (!resposta.data.content || resposta.data.content.length === 0) {
                toast.warn('Nenhum empréstimo encontrado.');
                return;
            }

            const emprestimosComNomes = await Promise.all(
                resposta.data.content.map(async (emprestimo) => {
                    try {
                        const respostaUsuario = await api.get(`/usuarios/${emprestimo.usuario?.id}`);
                        const nomeUsuario = respostaUsuario.data.nome;

                        const dataEmprestimoFormatada = isValid(parseISO(emprestimo.data_emprestimo)) ? format(parseISO(emprestimo.data_emprestimo), 'dd/MM/yyyy') : emprestimo.data_emprestimo;
                        const dataDevolucaoFormatada = isValid(parseISO(emprestimo.data_devolucao)) ? format(parseISO(emprestimo.data_devolucao), 'dd/MM/yyyy') : emprestimo.data_devolucao;

                        return {...emprestimo,
                            nomeUsuario: nomeUsuario || `Usuário ID: ${emprestimo.usuario?.id}`,
                            data_emprestimo: dataEmprestimoFormatada,
                            data_devolucao: dataDevolucaoFormatada,
                        };
                    } catch (erro) {
                        toast.error('Erro ao buscar o nome do usuário.');
                        console.error('Erro ao buscar o nome do usuário:', erro);
                        return emprestimo;
                    }
                })
            );
            setEmprestimos(emprestimosComNomes);
        } catch (erro) {
            toast.error('Erro ao buscar os empréstimos.');
            console.error('Erro ao buscar os empréstimos:', erro);
        }
    };

    const abrirModal = () => setMostrarModal(true);
    const fecharModal = () => {
        setMostrarModal(false);
        setDadosEmprestimo(null);
    };

    const editarEmprestimo = (emprestimo) => {
        setDadosEmprestimo(emprestimo);
        setMostrarModal(true);
    };

    const deletarEmprestimo = async (id) => {
        try {
            await api.delete(`/emprestimos/${id}`);
            toast.success('Empréstimo deletado com sucesso!');
            buscarEmprestimos();
        } catch (erro) {
            toast.error('Erro ao deletar o empréstimo.');
            console.error('Erro ao deletar o empréstimo:', erro);
        }
    };

    return (
        <div className="container">
            <h1>Gestão de Empréstimos</h1>
            <Button onClick={abrirModal}>Novo Empréstimo</Button>

            {emprestimos.length > 0 ? (
                <Table striped bordered hover className="mt-3">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuário</th>
                        <th>Livro ID</th>
                        <th>Data Empréstimo</th>
                        <th>Data Devolução</th>
                        <th>Status</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    {emprestimos.map((emprestimo) => (
                        <tr key={emprestimo.id}>
                            <td>{emprestimo.id}</td>
                            <td>{emprestimo.nomeUsuario || `Usuário ID: ${emprestimo.usuario?.id}`}</td>
                            <td>{emprestimo.livro?.id || 'Livro não encontrado'}</td>
                            <td>{emprestimo.data_emprestimo}</td>
                            <td>{emprestimo.data_devolucao}</td>
                            <td>{emprestimo.status}</td>
                            <td>
                                <Button variant="warning" onClick={() => editarEmprestimo(emprestimo)}>Editar</Button>{' '}
                                <Button variant="danger" onClick={() => deletarEmprestimo(emprestimo.id)}>Deletar</Button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            ) : (
                <p>Nenhum empréstimo encontrado.</p>
            )}

            <FormularioEmprestimos estaEditando={!!dadosEmprestimo} dadosEmprestimo={dadosEmprestimo} aoEnviarSucesso={() => {fecharModal();buscarEmprestimos();}} show={mostrarModal} onHide={fecharModal}/>
        </div>
    );
};

export default EmprestimosPage;
