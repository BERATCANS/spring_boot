import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'react-bootstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import StudentSearchBar from "./StudentSearchBar";

class StudentList extends Component {

    constructor(props) {
        super(props);
        this.state = {students: []};
        this.remove = this.remove.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
    }
    fetchAll() {
        fetch('api/v1/students')
            .then(response => response.json())
            .then(data => this.setState({students: data}));
    }
    componentDidMount() {
        fetch('api/v1/students')
            .then(response => response.json())
            .then(data => this.setState({students: data}));
    }

    async remove(id) {
        await fetch(`api/v1/students/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedStudents = [...this.state.students].filter(i => i.id !== id);
            this.setState({students: updatedStudents});
        });
    }
    handleSearch = async (query) => {
        if (!query) {
            this.fetchAll();
            return;
        }

        try {
            const response = await fetch(`/api/v1/students/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) throw new Error('Search failed');
            const data = await response.json();
            this.setState({ students: data });
        } catch (error) {
            console.error('Error searching:', error);
        }
    };


    render() {
        const {students} = this.state;


        const studentList = students.map(student => {
            return <tr key={student.id}>
                <td style={{whiteSpace: 'nowrap'}}>{student.name}</td>
                <td>{student.surname}</td>
                <td>{student.number}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" as={Link} to={"/students/" + student.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(student.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar />
                <Container fluid className="mt-3">
                    {/* ÜST ALAN */}
                <div className="d-flex justify-content-between align-items-center mb-5 flex-wrap">
                    <div>
                        <Button variant="success" as={Link} to="/students/new">Add Student</Button>
                    </div>

                    <div className="d-flex align-items-center mx-auto" style={{ maxWidth: '600px', flexGrow: 1, justifyContent: 'center' }}>
                        <StudentSearchBar onSearch={this.handleSearch} />
                    </div>

                    <div style={{ width: '100px' }}></div>
                </div>

                <Table className="mt-3">
                    <thead>
                    <tr>
                        <th width="20%">Name</th>
                        <th width="20%">Surname</th>
                        <th width="20%">Number</th>
                        <th width="40%">Actions</th>
                    </tr>
                    </thead>
                    <tbody>{studentList}</tbody>
                </Table>
            </Container>
            </div>
        );
    }
}

export default StudentList;