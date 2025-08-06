import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'react-bootstrap';
import AppNavbar from '../components/AppNavbar';
import { Link } from 'react-router-dom';
import StudentSearchBar from "../components/StudentSearchBar";
import { fetchAllStudents, deleteStudent, searchStudents } from '../services/studentService';

class StudentList extends Component {

    constructor(props) {
        super(props);
        this.state = {students: [],
        role: localStorage.getItem("role")
        };
        this.remove = this.remove.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
    }
    componentDidMount() {
        fetchAllStudents()
            .then(data => this.setState({ students: data }))
            .catch(error => console.error('Fetch error:', error));
    }

    fetchAll() {
        fetchAllStudents()
            .then(data => this.setState({ students: data }))
            .catch(error => console.error('Fetch error:', error));
    }

    async remove(id) {
        try {
            await deleteStudent(id);
            const updatedStudents = this.state.students.filter(s => s.id !== id);
            this.setState({ students: updatedStudents });
        } catch (error) {
            console.error('Delete error:', error);
        }
    }

    handleSearch = async (query) => {
        if (!query) {
            this.fetchAll();
            return;
        }
        try {
            const data = await searchStudents(query);
            this.setState({ students: data });
        } catch (error) {
            console.error('Search error:', error);
        }
    }


    render() {
        const {students, role} = this.state;

        const studentList = students.map(student => {
            return <tr key={student.id}>
                <td style={{whiteSpace: 'nowrap'}}>{student.name}</td>
                <td>{student.surname}</td>
                <td>{student.number}</td>
                <td>
                    {role === "ROLE_ADMIN" &&(
                    <ButtonGroup>
                        <Button size="sm" color="primary" as={Link} to={"/students/" + student.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(student.id)}>Delete</Button>
                        <Button size="sm" variant="info" as={Link} to={`/students/details/${student.id}`}>Details</Button>
                    </ButtonGroup>
                        )}
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar />
                <Container fluid className="mt-3">
                    {/* ÜST ALAN */}
                <div className="d-flex justify-content-between align-items-center mb-5 flex-wrap">
                    {role === "ROLE_ADMIN" && (
                    <div>
                        <Button variant="success" as={Link} to="/students/new">Add Student</Button>
                    </div>
                        )}
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
                        {role === "ROLE_ADMIN" ? (
                            <th width="40%">Actions</th>
                        ) : (
                            <th width="40%" colSpan="2"></th> // Boş başlık yerine colspan kullanın
                        )}
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