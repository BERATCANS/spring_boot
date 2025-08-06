export async function getStudentWithEnrollments(studentId) {
    const res = await fetch(`/api/v1/enrollments/${studentId}`, { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to fetch student');
    return await res.json();
}

export async function getAllLessons() {
    const res = await fetch('/api/v1/enrollments/lessons', { credentials: 'include' });
    if (!res.ok) throw new Error('Failed to fetch lessons');
    return await res.json();
}

export async function enrollStudent({ studentId, lessonId, year, semester }) {
    const params = new URLSearchParams({ studentId, lessonId, year, semester });
    const res = await fetch(`/api/v1/enrollments?${params.toString()}`, {
        method: 'POST',
        credentials: 'include'
    });

    if (!res.ok) {
        const errorData = await res.json();
        throw new Error(errorData.message || 'Enrollment failed');
    }

    return true;
}