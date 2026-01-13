const API_URL = "http://localhost:8080";
const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "index.html";
}

// Decode username from JWT
function getUsernameFromToken(token) {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.sub;
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
}

// Load user profile info
async function loadProfile() {
    try {
        const username = getUsernameFromToken(token);

        const response = await fetch(`${API_URL}/users/${username}`, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (response.status === 401 || response.status === 403) {
            logout();
            return;
        }

        const user = await response.json();
        document.getElementById("username").innerText = user.username;
        document.getElementById("email").innerText = user.email;

    } catch (err) {
        document.getElementById("error").innerText = "Failed to load profile";
    }
}

// Load posts
let currentPage = 0;
const pageSize = 5;

async function loadPosts(page = 0) {
    const username = getUsernameFromToken(token);
    const response = await fetch(`${API_URL}/users/${username}/posts?page=${page}&size=${pageSize}`, {
        headers: { "Authorization": `Bearer ${token}` }
    });

    const data = await response.json();
    const postsDiv = document.getElementById("posts");
    postsDiv.innerHTML = "";

    if (data.content.length === 0) {
        postsDiv.innerHTML = "<p>No posts yet.</p>";
        return;
    }

    data.content.forEach(post => {
        const div = document.createElement("div");
        div.style.border = "1px solid #ccc";
        div.style.padding = "5px";
        div.style.marginBottom = "5px";
        div.innerText = post.content;
        postsDiv.appendChild(div);
    });

    currentPage = data.number;
    document.getElementById("prevBtn").disabled = data.first;
    document.getElementById("nextBtn").disabled = data.last;
}

function prevPage() {
    if (currentPage > 0) loadPosts(currentPage - 1);
}
function nextPage() {
    loadPosts(currentPage + 1);
}

// Create a post
async function createPost() {
    const content = document.getElementById("postContent").value;
    if (!content) return;

    try {
        const username = getUsernameFromToken(token);

        const response = await fetch(`${API_URL}/users/${username}/posts`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            },
            body: JSON.stringify({ content })
        });

        if (!response.ok) throw new Error("Failed to create post");

        document.getElementById("postContent").value = "";
        loadPosts(currentPage);

    } catch (err) {
        document.getElementById("postError").innerText = err.message;
    }
}

// Upload profile picture
async function uploadProfilePic() {
    const fileInput = document.getElementById("profilePicInput");
    const file = fileInput.files[0];
    if (!file) return;

    const username = getUsernameFromToken(token);
    const formData = new FormData();
    formData.append("file", file);

    try {
        const response = await fetch(`${API_URL}/users/${username}/profile-picture`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${token}`
            },
            body: formData
        });

        if (!response.ok) throw new Error("Upload failed");

        loadProfilePic(); // reload the image
    } catch (err) {
        document.getElementById("picError").innerText = err.message;
    }
}

// Load profile picture
async function loadProfilePic() {
  const username = getUsernameFromToken(token);

  try {
    const response = await fetch(
      `${API_URL}/users/${username}/profile-picture`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );

    if (!response.ok) {
      document.getElementById("profilePic").src = "default-profile.png";
      return;
    }

    const blob = await response.blob();
    document.getElementById("profilePic").src =
      URL.createObjectURL(blob);

  } catch (err) {
    document.getElementById("profilePic").src = "default-profile.png";
  }
}


// Initialize
loadProfile();
loadPosts();
loadProfilePic();
