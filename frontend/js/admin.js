async function loadUsers() {
  try {
    const res = await fetch(`${API_URL}/users`, {
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error("Access denied");

    const users = await res.json();
    const table = document.getElementById("userTable");
    table.innerHTML = "";

    users.forEach(u => {
      table.innerHTML += `
        <tr>
          <td>${u.username}</td>
          <td>${u.email}</td>
          <td>${u.role}</td>
          <td>
            <button onclick="deleteUser('${u.username}')">Delete</button>
          </td>
        </tr>
      `;
    });
  } catch (e) {
    document.getElementById("adminError").innerText = e.message;
  }
}

loadUsers();

async function deleteUser(username) {
  if (!confirm("Are you sure?")) return;

  const res = await fetch(`${API_URL}/users/${username}`, {
    method: "DELETE",
    headers: {
      "Authorization": `Bearer ${token}`
    }
  });

  if (res.status === 204) {
    loadUsers();
  } else {
    alert("Delete failed");
  }
}
