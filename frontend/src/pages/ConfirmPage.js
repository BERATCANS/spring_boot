import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'react-bootstrap';
import AppNavbar from '../components/AppNavbar';
import { fetchConfirmingStudents, acceptStudent, rejectStudent } from '../services/studentService';

class ConfirmPage extends Component {
    constructor(props) {
        super(props);
        this.state = { confirmingStudents: [] };
        this.confirmStudent = this.confirmStudent.bind(this);
        this.rejectStudent = this.rejectStudent.bind(this);
    }

    componentDidMount() {
        this.loadConfirmingStudents();
    }

    loadConfirmingStudents() {
        fetchConfirmingStudents()
            .then(data => this.setState({ confirmingStudents: data }))
            .catch(error => console.error('Fetch error:', error));
    }

    async confirmStudent(studentId) {
        try {
            await acceptStudent(studentId);
            this.loadConfirmingStudents();
        } catch (error) {
            console.error('Confirm error:', error);
        }
    }

    async rejectStudent(studentId) {
        try {
            await rejectStudent(studentId);
            this.loadConfirmingStudents();
        } catch (error) {
            console.error('Reject error:', error);
        }
    }

    render() {
        const { confirmingStudents } = this.state;

        const studentList = confirmingStudents.map(student => (
            <tr key={student.id}>
                <td style={{ whiteSpace: 'nowrap' }}>{student.name}</td>
                <td>{student.surname}</td>
                <td>{student.number}</td>
                <td>
                    <ButtonGroup>
                        <Button variant="success" size="sm" onClick={() => this.confirmStudent(student.id)}>✔</Button>
                        <Button variant="danger" size="sm" onClick={() => this.rejectStudent(student.id)}>❌</Button>
                    </ButtonGroup>
                </td>
            </tr>
        ));

        return (
            <div>
                <AppNavbar />
                <Container fluid className="mt-3">
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

export default ConfirmPage;