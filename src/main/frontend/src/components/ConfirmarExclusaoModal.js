import React from 'react';
import { Modal, Button, Table } from 'react-bootstrap';
const ConfirmarExclusaoModal = ({ show, onHide, emprestimos, onConfirm, tipo }) => {
    return (
        <Modal show={show} onHide={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{tipo === 'livro' ? 'Livro possui empréstimos' : 'Usuário possui empréstimos'}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>Este {tipo} possui os seguintes empréstimos:</p>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Usuário</th>
                        <th>Livro</th>
                        <th>Data Empréstimo</th>
                        <th>Data Devolução</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {emprestimos.map((emprestimo) => (
                        <tr key={emprestimo.id}>
                            <td>{emprestimo.id}</td>
                            <td>{emprestimo.usuario.nome}</td>
                            <td>{emprestimo.livro.titulo}</td>
                            <td>{emprestimo.dataEmprestimo}</td>
                            <td>{emprestimo.dataDevolucao}</td>
                            <td>{emprestimo.status}</td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
                <p>Deseja excluir o {tipo} e todos os seus empréstimos?</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>Cancelar</Button>
                <Button variant="danger" onClick={onConfirm}>Excluir Tudo</Button>
            </Modal.Footer>
        </Modal>
    );
};

export default ConfirmarExclusaoModal;
