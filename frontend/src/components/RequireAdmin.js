import React from 'react';
import { Navigate } from 'react-router-dom';

function RequireAdmin({ children }) {
    const role = localStorage.getItem("role");

    if (role !== "ROLE_ADMIN") {
        return <Navigate to="/students" replace />;
    }

    return children;
}

export default RequireAdmin;
