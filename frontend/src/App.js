import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Home from './Home';
import StudentList from './StudentList';
import StudentEdit from './StudentEdit';

function App() {
    return (
        <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/students" element={<StudentList />} />
            <Route path="/students/:id" element={<StudentEdit />} />
        </Routes>
    );
}

export default App;
// This code defines the main application component for a React application.