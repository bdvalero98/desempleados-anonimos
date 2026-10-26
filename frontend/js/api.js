import { API_URL }  from "./config.js";

async function pedirJson(url, opciones) {
    const respuesta = await fetch(url, opciones);
    const cuerpo = await respuesta.json().catch(() => null);

    if (!respuesta.ok) {
        throw new Error(cuerpo?.mensaje ?? "Algo salió mal. Inténtalo de nuevo.");
    }

    return cuerpo;
}

const enJson = (datos) => ({
    method: "POST",
    headers: { "Content-Type": "application/json"},
    body: JSON.stringify(datos),
});

export const listar = () => pedirJson(API_URL);

export const publicar = (mensaje, diasDesempleado) =>
    pedirJson(API_URL, enJson({ mensaje, diasDesempleado}));

export const reaccionar = (id, tipoReaccion) =>
    pedirJson(`${API_URL}/${id}/reacciones`, enJson( {tipoReaccion} ));

export function escucharEnVivo( { alRecibir, alConectar, alDesconectar } ) {
    const fuente = new EventSource(`${API_URL}/stream`);

    fuente.onmessage = (evento) => alRecibir(JSON.parse(evento.data));
    fuente.onopen = alConectar;
    fuente.onerror = alDesconectar;
    
    return fuente;
}