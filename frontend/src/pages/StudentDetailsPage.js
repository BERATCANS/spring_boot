import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
    getStudentWithEnrollments,
    getAllLessons,
    enrollStudent
} from '../services/EnrollmentService';

const StudentDetailsPage = () => {
    const { studentId } = useParams();
    const [student, setStudent] = useState(null);
    const [lessons, setLessons] = useState([]);
    const [enrollmentForm, setEnrollmentForm] = useState({
        lessonId: '',
        year: new Date().getFullYear(),
        semester: 'FALL'
    });

    useEffect(() => {
        if (!studentId) return;

        getStudentWithEnrollments(studentId)
            .then(setStudent)
            .catch(console.error);

        getAllLessons()
            .then(setLessons)
            .catch(console.error);
    }, [studentId]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEnrollmentForm(prev => ({ ...prev, [name]: value }));
    };

    const handleEnroll = (e) => {
        e.preventDefault();
        enrollStudent({
            studentId,
            lessonId: enrollmentForm.lessonId,
            year: enrollmentForm.year,
            semester: enrollmentForm.semester
        })
            .then(() => {
                alert('Student enrolled successfully');
                window.location.reload();
            })
            .catch(() => alert('Failed to enroll student'));
    };

    if (!student) return <div style={styles.loading}>Loading student...</div>;

    return (
        <div style={styles.container}>
            <h2 style={styles.title}>
                {student.name} {student.surname} <span style={styles.number}>#{student.number}</span>
            </h2>

            <section style={styles.section}>
                <h3 style={styles.sectionTitle}>Enrollments</h3>
                {student.enrollments.length === 0 ? (
                    <p style={styles.noEnrollment}>No enrollments found.</p>
                ) : (
                    <ul style={styles.list}>
                        {student.enrollments.map((enrollment, idx) => (
                            <li key={idx} style={styles.listItem}>
                                <strong>{enrollment.lessonId}</strong> — {enrollment.semester} {enrollment.year}
                            </li>
                        ))}
                    </ul>
                )}
            </section>

            <section style={styles.section}>
                <h3 style={styles.sectionTitle}>Enroll in a New Lesson</h3>
                <form style={styles.form} onSubmit={handleEnroll}>
                    <select
                        name="lessonId"
                        value={enrollmentForm.lessonId}
                        onChange={handleChange}
                        required
                        style={styles.select}
                    >
                        <option value="">Select a lesson</option>
                        {lessons.map(lesson => (
                            <option key={lesson.id} value={lesson.id}>
                                {lesson.id}
                            </option>
                        ))}
                    </select>

                    <input
                        type="number"
                        name="year"
                        value={enrollmentForm.year}
                        onChange={handleChange}
                        required
                        min="2000"
                        max={new Date().getFullYear() + 5}
                        style={styles.input}
                    />

                    <select
                        name="semester"
                        value={enrollmentForm.semester}
                        onChange={handleChange}
                        required
                        style={styles.select}
                    >
                        <option value="FALL">FALL</option>
                        <option value="SPRING">SPRING</option>
                    </select>

                    <button type="submit" style={styles.button}>Enroll</button>
                </form>
            </section>
        </div>
    );
};

const styles = {
    container: {
        maxWidth: '600px',
        margin: '40px auto',
        padding: '20px',
        fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
        backgroundColor: '#f9f9f9',
        borderRadius: '10px',
        boxShadow: '0 4px 10px rgba(0,0,0,0.1)'
    },
    title: {
        fontSize: '28px',
        marginBottom: '10px',
        color: '#333',
        borderBottom: '2px solid #007bff',
        paddingBottom: '5px'
    },
    number: {
        fontWeight: 'normal',
        color: '#777',
        fontSize: '18px',
        marginLeft: '8px'
    },
    section: {
        marginTop: '30px'
    },
    sectionTitle: {
        fontSize: '22px',
        marginBottom: '15px',
        color: '#007bff'
    },
    noEnrollment: {
        fontStyle: 'italic',
        color: '#666'
    },
    list: {
        listStyleType: 'none',
        paddingLeft: '0'
    },
    listItem: {
        backgroundColor: 'white',
        padding: '10px 15px',
        borderRadius: '6px',
        boxShadow: '0 1px 3px rgba(0,0,0,0.1)',
        marginBottom: '10px',
        fontSize: '16px',
        color: '#333'
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: '15px',
    },
    select: {
        padding: '10px',
        fontSize: '16px',
        borderRadius: '6px',
        border: '1px solid #ccc',
        outline: 'none',
        transition: 'border-color 0.3s',
    },
    input: {
        padding: '10px',
        fontSize: '16px',
        borderRadius: '6px',
        border: '1px solid #ccc',
        outline: 'none',
        transition: 'border-color 0.3s',
    },
    button: {
        padding: '12px',
        fontSize: '18px',
        backgroundColor: '#007bff',
        color: 'white',
        border: 'none',
        borderRadius: '8px',
        cursor: 'pointer',
        fontWeight: '600',
        alignSelf: 'flex-start',
        boxShadow: '0 4px 8px rgba(0,123,255,0.3)',
        transition: 'background-color 0.3s ease',
    },
    loading: {
        textAlign: 'center',
        fontSize: '20px',
        marginTop: '50px',
        color: '#666'
    }
};

export default StudentDetailsPage;
