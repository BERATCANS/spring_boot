const BASE_URL = '/api/v1/students';

export async function fetchAllStudents() {
    const response = await fetch(BASE_URL, {
        method: 'GET',
        credentials: 'include'
    });
    if (!response.ok) throw new Error('Failed to fetch students');
    return await response.json();
}

export async function deleteStudent(id) {
    const response = await fetch(`${BASE_URL}/${id}`, {
        method: 'DELETE',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    });
    if (!response.ok) throw new Error('Failed to delete student');
}

export async function searchStudents(query) {
    const response = await fetch(`${BASE_URL}/search?query=${encodeURIComponent(query)}`, {
        method: 'GET',
        credentials: 'include'
    });
    if (!response.ok) throw new Error('Search failed');
    return await response.json();
}

export async function getStudentById(id) {
    const response = await fetch(`${BASE_URL}/${id}`);
    if (!response.ok) {
        throw new Error('Student not found');
    }
    return await response.json();
}

export async function saveStudent(student) {
    const isNew = !student.id;
    const url = isNew ? BASE_URL : `${BASE_URL}/${student.id}`;
    const method = isNew ? 'POST' : 'PUT';

    const response = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(student)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Unknown error occurred');
    }
}
