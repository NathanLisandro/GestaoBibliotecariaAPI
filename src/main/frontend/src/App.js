import React from 'react';
import { HashRouter as Router, Routes, Route } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import GlobalStyles from './styles/GlobalStyles';
import Navbar from './components/Navbar';
import UsuariosPage from './pages/UsuariosPage';
import LivrosPage from './pages/LivrosPage';
import EmprestimosPage from './pages/EmprestimosPage';
import RecomendacoesPage from './pages/RecomendacoesPage';
import GoogleBooksPage from './pages/GoogleBooksPage';

function App() {
    return (
        <>
            <GlobalStyles/>
            <Router>
                <Navbar/>
                <Routes>
                    <Route path="/" element={<UsuariosPage/>}/>
                    <Route path="/livros" element={<LivrosPage/>}/>
                    <Route path="/emprestimos" element={<EmprestimosPage/>}/>
                    <Route path="/recomendacoes" element={<RecomendacoesPage/>}/>
                    <Route path="/googlebooks" element={<GoogleBooksPage/>}/>
                </Routes>
            </Router>
            <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} newestOnTop={false} closeOnClick rtl={false} pauseOnFocusLoss draggable pauseOnHover/>
        </>
    );
}

export default App;
