const API_URL = "http://localhost:8080";

async function login() {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch(`${API_URL}/auth/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (!response.ok) {
      throw new Error("Invalid credentials");
    }

    const data = await response.json();
    localStorage.setItem("token", data.token);

    window.location.href = "profile.html";
  } catch (err) {
    document.getElementById("error").innerText = err.message;
  }
}

async function register() {
  const username = document.getElementById("username").value;
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  try {
    const response = await fetch(`${API_URL}/users`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, email, password })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(Object.values(errorData).join(", "));
    }

    document.getElementById("success").innerText =
      "Registration successful! Please login.";

    document.getElementById("error").innerText = "";

  } catch (err) {
    document.getElementById("error").innerText = err.message;
  }
}

