import React from 'react';
import {Routes, Route, Navigate} from 'react-router-dom';
import Home from './pages/Home';
import StudentList from './pages/StudentList';
import StudentEdit from './pages/StudentEdit';
import RegisterPage from "./pages/RegisterPage";
import RequireAdmin from "./components/RequireAdmin";
import RequireAuth from "./components/RequireAuth";
import ConfirmPage from "./pages/ConfirmPage";
function App() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="/login" element={<Home />} />
            <Route path="/students" element={
                <RequireAuth>
                    <StudentList />
                </RequireAuth>
            } />

            <Route path="/students/:id" element={
                <RequireAdmin>
                    <StudentEdit />
                </RequireAdmin>
            } />

            <Route path="/register" element={
                <RequireAdmin>
                    <RegisterPage />
                </RequireAdmin>
            } />

            <Route path="/confirm" element={
                <RequireAdmin>
                    <ConfirmPage />
                </RequireAdmin>
            } />
        </Routes>
    );
}

export default App;
