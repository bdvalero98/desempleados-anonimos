import * as api from "./api.js";

const muro = document.getElementById("muro");
const vacio = document.getElementById("vacio");
const formulario = document.getElementById("formulario");
const campoMensaje = document.getElementById("mensaje");
const campoDias = document.getElementById("dias");
const contador = document.getElementById("contador");
const botonEnviar = document.getElementById("enviar");
const error = document.getElementById("error");
const estado = document.getElementById("estado");

const tarjetas = new Map();

function yaReaccionaste(id, tipoReaccion) {
    try {
        return localStorage.getItem(`reaccion:${id}:${tipoReaccion}`) === "1"; 
    } catch (error) {
        return false;
    }
}

function recordarReaccion(id, tipoReaccion) {
    try {
        localStorage.setItem(`reaccion:${id}:${tipoReaccion}`, "1");
    } catch (error) {
        
    }
}

function crearBoton(publicacion, tipoReaccion, etiqueta, campo) {
    const boton = document.createElement("button");

    boton.type = "button";
    boton.className = "reaccion";
    boton.dataset.etiqueta = "etiqueta";
    boton.dataset.campo = "campo";
    boton.textContent = `${etiqueta} · ${publicacion[campo]}`;
    boton.disabled = yaReaccionaste(publicacion.id, tipoReaccion);

    boton.addEventListener("click", async () => {
        boton.disabled = true;

        try {
            const actualizada = await api.reaccionar(publicacion.id, tipoReaccion);
            recordarReaccion(publicacion.id, tipoReaccion);
            mostrar(actualizada);
        } catch (fallo) {
            boton.disabled = false;
            mostrarError(fallo.message);
        }
    });

    return boton;
}

function crearTarjeta(publicacion) {
    const tarjeta = document.createElement("article");
    tarjeta.className = "tarjeta desahogo";

    const cabecera = document.createElement("header");

    const alias = document.createElement("strong");
    alias.textContent = publicacion.alias;

    const dias = document.createElement("span");
    dias.className = "dias";
    dias.textContent = publicacion.diasDesempleado != null ? `${publicacion.diasDesempleado} dias desempleado` : "";

    cabecera.append(alias, dias);

    const texto = document.createElement("p");
    texto.textContent = publicacion.mensaje;

    const pie = document.createElement("footer");
    pie.append(
        crearBoton(publicacion, "YO_TAMBIEN", "Yo también", "yoTambien"),
        crearBoton(publicacion, "ANIMO", "Ánimo", "animo"),
    );

    tarjeta.append(cabecera, texto, pie);
    return tarjeta;
}

function actualizarContadores(tarjeta, publicacion) {
    tarjeta.querySelectorAll(".reaccion").forEach(
        (boton) => {
            boton.textContent = `${boton.dataset.etiqueta} · ${publicacion[boton.dataset.campo]}`;
        }
    );
}

function mostrar(publicacion) {
    const existente = tarjetas.get(publicacion.id);

    if (existente) {
        actualizarContadores(existente, publicacion);
        return;
    }

    const tarjeta = crearTarjeta(publicacion);
    tarjetas.set(publicacion.id, tarjeta);
    muro.prepend(tarjeta);
    vacio.hidden = true;
}

function limpiarMuro() {
    muro.replaceChildren();
    tarjetas.clear();
    vacio.hidden = false;
}

function mostrarError(mensaje) {
    error.textContent = mensaje;
}

async function cargarMuro() {
    try {
        const lista = await api.listar();
        limpiarMuro();
        [...lista].reverse().forEach(mostrar);
    } catch (fallo) {
        mostrarError("No pudimos cargar el muro.");
    }
}

api.escucharEnVivo({
    alRecibir: mostrar,
    alConectar: () => {
        estado.textContent = "En vivo";
        cargarMuro();
    },
    alDesconectar: () => {
        estado.textContent = "Reconectando...";
    },
});

campoMensaje.addEventListener("input", () => {
    contador.textContent = campoMensaje.value.length;
});

formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    mostrarError("");
    botonEnviar.disabled = true;

    const dias = campoDias.value === "" ? null : Number(campoDias.value);

    try {
        const creada = await api.publicar(campoMensaje.value, dias);
        mostrar(creada);
        formulario.reset();
        contador.textContent = "0";
    } catch (fallo) {
        mostrarError(fallo.mensaje);
    } finally {
        botonEnviar.disabled = false;
    }
});