import React from 'react';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

const Navbar = () => {
    return (
        <Nav>
            <ul>
                <li><Link to="/">Usuários</Link></li>
                <li><Link to="/livros">Livros</Link></li>
                <li><Link to="/emprestimos">Emprestimos</Link></li>
                <li><Link to="/recomendacoes">Recomendações</Link></li>
                <li><Link to="/googlebooks">Google Books</Link></li>
            </ul>
        </Nav>
    );
};

const Nav = styled.nav`
  background: #4CAF50;
  padding: 10px;

  ul {
    display: flex;
    list-style: none;
    justify-content: space-around;
  }

  a {
    color: white;
    font-weight: bold;
  }
`;

export default Navbar;
