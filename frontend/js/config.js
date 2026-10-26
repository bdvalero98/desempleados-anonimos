const ejecutandoEnLocal = ["localhost", "127.0.0.1"].includes(location.hostname);

export const API_URL = ejecutandoEnLocal ? "http://localhost:8080/api/desahogos" : "https://TU-BACKEND.onrender.com/api/desahogos";