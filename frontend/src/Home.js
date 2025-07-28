import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'react-bootstrap';

class Home extends Component {
    render() {
        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <Button as={Link} to="/students" style={{ color: 'white' }} >
                        Students
                    </Button>
                </Container>
            </div>
        );
    }
}
export default Home;
// This code defines a simple home page component for a React application.