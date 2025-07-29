import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import StudentList from './pages/StudentList';
import StudentEdit from './pages/StudentEdit';
import RegisterPage from "./pages/RegisterPage";
import RequireAdmin from "./components/RequireAdmin";

function App() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/students" element={<StudentList />} />
            <Route path="/students/:id" element={<StudentEdit />} />
            <Route path="/register" element={
                <RequireAdmin>
                    <RegisterPage />
                </RequireAdmin>
            } />
        </Routes>
    );
}

export default App;
// This code defines the main application component for a React application.