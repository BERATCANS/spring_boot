import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Container, Form, Toast, ToastContainer } from 'react-bootstrap';
import AppNavbar from '../components/AppNavbar';
import {getStudentById, saveStudent} from "../services/studentService";

function StudentEdit() {
    const emptyItem = {
        name: '',
        surname: '',
        number: ''
    };

    const [item, setItem] = useState(emptyItem);
    const [error, setError] = useState('');
    const [showToast, setShowToast] = useState(false);

    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(() => {
        if (id !== 'new') {
            getStudentById(id)
                .then(data => setItem(data))
                .catch(err => {
                    setError(err.message);
                    setShowToast(true);
                });
        }
    }, [id]);

    const handleChange = event => {
        const { name, value } = event.target;
        setItem({ ...item, [name]: value });
    };

    const handleSubmit = async event => {
        event.preventDefault();
        try {
            await saveStudent(item);
            navigate('/students');
        } catch (err) {
            setError(err.message);
            setShowToast(true);
        }
    };

    const title = <h2>{item.id ? 'Edit Student' : 'Add Student'}</h2>;

    return (
        <div>
            <AppNavbar />
            <Container>
                {title}
                <Form onSubmit={handleSubmit}>
                    <Form.Group className="mb-3">
                        <Form.Label>Name</Form.Label>
                        <Form.Control
                            type="text"
                            name="name"
                            value={item.name}
                            onChange={handleChange}
                            placeholder="Enter name"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Surname</Form.Label>
                        <Form.Control
                            type="text"
                            name="surname"
                            value={item.surname}
                            onChange={handleChange}
                            placeholder="Enter surname"
                        />
                    </Form.Group>
                    <Form.Group className="mb-3">
                        <Form.Label>Number</Form.Label>
                        <Form.Control
                            type="text"
                            name="number"
                            value={item.number}
                            onChange={handleChange}
                            placeholder="Enter number"
                        />
                    </Form.Group>
                    <Button variant="primary" type="submit">Submit</Button>
                </Form>

                {/* Toast gösterimi */}
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
        </div>
    );
}

export default StudentEdit;
