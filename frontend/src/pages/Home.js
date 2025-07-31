import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {login} from "../services/userService";
import {Button, Container, Form, Toast, ToastContainer} from "react-bootstrap";

function Home() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [showToast, setShowToast] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const result = await login(username, password);
            localStorage.setItem("role", result.role);
            navigate("/students");
        } catch (err) {
            alert(err.message);
        }
    };


    return (
        <Container className="mt-4" style={{ maxWidth: '400px' }}>
            <h2>Login</h2>
            <Form onSubmit={e => { e.preventDefault(); handleLogin(); }}>
                <Form.Group className="mb-3">
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                        type="text"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        placeholder="Enter username"
                    />
                </Form.Group>
                <Form.Group className="mb-3">
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        type="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        placeholder="Enter password"
                    />
                </Form.Group>
                <Button variant="primary" type="submit">Login</Button>
            </Form>

            <ToastContainer position="top-end" className="p-3">
                <Toast
                    bg="danger"
                    onClose={() => setShowToast(false)}
                    show={showToast}
                    delay={4000}
                    autohide
                >
                    <Toast.Header>
                        <strong className="me-auto">Error</strong>
                    </Toast.Header>
                    <Toast.Body className="text-white">{error}</Toast.Body>
                </Toast>
            </ToastContainer>
        </Container>
    );
}


export default Home;
