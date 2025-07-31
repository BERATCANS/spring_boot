import { Navigate } from "react-router-dom";

function RequireAuth({ children }) {
    const role = localStorage.getItem("role");

    if (!role) {
        return <Navigate to="/login" />;
    }

    return children;
}

export default RequireAuth;
