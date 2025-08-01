export async function login(username, password) {
    const response = await fetch("/api/v1/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Login failed");
    }

    return response.json();
}

export async function register(username, password, isAdmin) {
    const params = new URLSearchParams({ isAdmin });

    const response = await fetch(`/api/v1/users/register?${params}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Registration failed");
    }

    return response.json(); // Optional, if your backend returns anything
}

export async function logout() {
    const response = await fetch("/api/v1/users/logout", {
        method: "POST",
        credentials: "include"
    });

    if (!response.ok) {
        const error = await response.text(); // logout genelde text döner
        throw new Error(error || "Logout failed");
    }

    return response.text(); // "Logout successful" gibi bir yanıt bekleniyor
}

