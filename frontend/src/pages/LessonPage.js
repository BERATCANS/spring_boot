import React, { useEffect, useState } from 'react';

const LessonPage = () => {
    const [lessons, setLessons] = useState([]);
    const [lessonForm, setLessonForm] = useState({ id: '', name: '' });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Dersleri yükle
    useEffect(() => {
        fetchLessons();
    }, []);

    const fetchLessons = async () => {
        try {
            setLoading(true);
            const res = await fetch('/api/v1/enrollments/lessons', { credentials: 'include' });
            if (!res.ok) throw new Error('Failed to fetch lessons');
            const data = await res.json();
            setLessons(data);
            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
    };

    // Form input değişimi
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setLessonForm(prev => ({ ...prev, [name]: value }));
    };

    // Yeni ders ekle
    const handleAddLesson = async (e) => {
        e.preventDefault();
        if (!lessonForm.name) {
            alert('Lesson name is required');
            return;
        }
        try {
            const res = await fetch('/api/v1/enrollments/lessons', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify(lessonForm),
            });
            if (!res.ok) {
                const errorData = await res.json();
                throw new Error(errorData.message || 'Failed to add lesson');
            }
            setLessonForm({ id: '', name: '' });
            fetchLessons(); // Listeyi güncelle
        } catch (err) {
            alert(err.message);
        }
    };

    // Ders sil
    const handleDeleteLesson = async (id) => {
        if (!window.confirm('Are you sure you want to delete this lesson?')) return;
        try {
            const res = await fetch(`/api/v1/enrollments/lessons/${id}`, {
                method: 'DELETE',
                credentials: 'include',
            });
            if (!res.ok) throw new Error('Failed to delete lesson');
            fetchLessons();
        } catch (err) {
            alert(err.message);
        }
    };

    return (
        <div style={{ maxWidth: 600, margin: '20px auto', fontFamily: 'Arial' }}>
            <h2>Lesson Management</h2>

            <form onSubmit={handleAddLesson} style={{ marginBottom: '20px' }}>
                <div style={{ marginBottom: '10px' }}>
                    <label>
                        Lesson ID:&nbsp;
                        <input
                            type="text"
                            name="id"
                            value={lessonForm.id}
                            onChange={handleInputChange}
                            placeholder="e.g. MATH101"
                            style={{ width: '100px' }}
                        />
                    </label>
                </div>

                <div style={{ marginBottom: '10px' }}>
                    <label>
                        Lesson Name:*&nbsp;
                        <input
                            type="text"
                            name="name"
                            value={lessonForm.name}
                            onChange={handleInputChange}
                            placeholder="e.g. Mathematics"
                            required
                            style={{ width: '300px' }}
                        />
                    </label>
                </div>

                <button type="submit" style={{ padding: '8px 15px' }}>Add Lesson</button>
            </form>

            <h3>Existing Lessons</h3>

            {loading ? (
                <p>Loading lessons...</p>
            ) : error ? (
                <p style={{ color: 'red' }}>{error}</p>
            ) : lessons.length === 0 ? (
                <p>No lessons found.</p>
            ) : (
                <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                    <thead>
                    <tr style={{ borderBottom: '1px solid #ccc' }}>
                        <th style={{ textAlign: 'left', padding: '8px' }}>ID</th>
                        <th style={{ textAlign: 'left', padding: '8px' }}>Name</th>
                        <th style={{ textAlign: 'left', padding: '8px' }}>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {lessons.map(lesson => (
                        <tr key={lesson.id} style={{ borderBottom: '1px solid #eee' }}>
                            <td style={{ padding: '8px' }}>{lesson.id || '-'}</td>
                            <td style={{ padding: '8px' }}>{lesson.name}</td>
                            <td style={{ padding: '8px' }}>
                                <button
                                    style={{ color: 'white', backgroundColor: 'red', border: 'none', padding: '5px 10px', cursor: 'pointer' }}
                                    onClick={() => handleDeleteLesson(lesson.id)}
                                >
                                    Delete
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default LessonPage;
