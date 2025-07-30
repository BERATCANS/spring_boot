import React, { Component } from 'react';
import { Navbar, Container, Nav } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            role: localStorage.getItem("role") || null
        };
    }

    render() {
        const { role } = this.state;

        return (
            <Navbar bg="dark" variant="dark" expand="md">
                <Container>
                    <Navbar.Brand as={Link} to="/">HOME</Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/students">Students</Nav.Link>
                    </Nav>
                    <Nav className="ms-auto">
                        {role === "ROLE_ADMIN" && (
                            <Nav.Link as={Link} to="/register">Register</Nav.Link>
                        )}
                        <Nav.Link as={Link} to="/">Logout</Nav.Link>
                    </Nav>
                </Container>
            </Navbar>
        );
    }
}
