import React, { Component } from 'react';
import { Navbar, Container, Nav } from 'react-bootstrap';
import { Link, Navigate } from 'react-router-dom';
import { logout } from '../services/userService';

export default class AppNavbar extends Component {
    constructor(props) {
        super(props);
        this.state = {
            role: localStorage.getItem("role") || null,
            redirectToLogin: false
        };
    }

    handleLogout = async () => {
        try {
            await logout();
            localStorage.removeItem("role");
            this.setState({ redirectToLogin: true });
        } catch (error) {
            console.error("Logout failed:", error);
            alert("Logout failed: " + error.message);
        }
    };

    render() {
        const { role, redirectToLogin } = this.state;

        if (redirectToLogin) {
            return <Navigate to="/login" />;
        }

        return (
            <Navbar bg="dark" variant="dark" expand="md">
                <Container>
                    <Navbar.Brand as={Link} to="/login">HOME</Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/students">Students</Nav.Link>
                        {role === "ROLE_ADMIN" && (
                            <>
                                <Nav.Link as={Link} to="/confirm">Confirm</Nav.Link>
                                <Nav.Link as={Link} to="/lessons">Lessons</Nav.Link>
                            </>
                        )}
                    </Nav>
                    <Nav className="ms-auto">
                        {role === "ROLE_ADMIN" && (
                            <Nav.Link as={Link} to="/register">Register</Nav.Link>
                        )}
                        <Nav.Link onClick={this.handleLogout}>Logout</Nav.Link>
                    </Nav>
                </Container>
            </Navbar>
        );
    }
}
