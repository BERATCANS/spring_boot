import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { register } from '../services/userService';
import {Button, Container, Form, Toast, ToastContainer} from "react-bootstrap";

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isAdmin, setIsAdmin] = useState(false);
    const [showToast, setShowToast] = useState(false); // Tanımlama
    const [error, setError] = useState(''); // Tanımlama
    const navigate = useNavigate();

    const handleRegister = async () => {
        try {
            await register(username, password, isAdmin);
            alert("Registration successful!");
            navigate("/students");
        } catch (error) {
            alert(error.message || "Registration failed");
        }
    };

    return (
        <Container className="mt-4" style={{ maxWidth: '400px' }}>
            <h2>Register</h2>
            <Form onSubmit={e => { e.preventDefault(); handleRegister(); }}>
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
                <Form.Group className="mb-3">
                    <Form.Check
                        type="checkbox"
                        label="Register as Admin"
                        checked={isAdmin}
                        onChange={e => setIsAdmin(e.target.checked)}
                    />
                </Form.Group>
                <Button variant="primary" type="submit">Register</Button>
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

export default RegisterPage;