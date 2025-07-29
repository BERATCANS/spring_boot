import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async () => {
        const params = new URLSearchParams({ isAdmin });
        const res = await fetch(`/api/users/register?${params}`, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({ username, password })
        });

        if (res.ok) {
            alert("Registration successful!");
            navigate("/students");
        } else {
            const error = await res.json();
            alert(error.message || "Registration failed");
        }
    };

    return (
        <div>
            <h2>Register</h2>
            <input value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" />
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" />
            <label>
                <input type="checkbox" checked={isAdmin} onChange={e => setIsAdmin(e.target.checked)} />
                Register as Admin
            </label>
            <button onClick={handleRegister}>Register</button>
        </div>
    );
}

export default RegisterPage;
