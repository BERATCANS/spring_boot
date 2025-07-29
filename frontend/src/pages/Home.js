import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {login} from "../services/userService";

function Home() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const result = await login(username, password);
            localStorage.setItem("role", result.role);
        } catch (err) {
            alert(err.message);
        }
    };


    return (
        <div>
            <h2>Login</h2>
            <input value={username} onChange={e => setUsername(e.target.value)} placeholder="Username" />
            <input type="password" value={password} onChange={e => setPassword(e.target.value)} placeholder="Password" />
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default Home;
