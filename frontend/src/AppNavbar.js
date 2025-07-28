import React, { Component } from 'react';
import { Navbar, Container, Nav } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export default class AppNavbar extends Component {
    render() {
        return (
            <Navbar bg="dark" variant="dark" expand="md">
                <Container>
                    <Navbar.Brand as={Link} to="/">HOME</Navbar.Brand>
                    <Nav className="me-auto">
                        <Nav.Link as={Link} to="/students">Students</Nav.Link>
                    </Nav>
                </Container>
            </Navbar>
        );
    }
}