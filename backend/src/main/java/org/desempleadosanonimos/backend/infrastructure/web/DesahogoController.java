package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.application.usecase.ListarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.ObservarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.PublicarDesahogo;
import org.desempleadosanonimos.backend.application.usecase.ReaccionarADesahogo;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/desahogos")
public class DesahogoController {

    private final PublicarDesahogo publicarDesahogo;
    private final ListarDesahogos listarDesahogos;
    private final ObservarDesahogos observarDesahogos;
    private final ReaccionarADesahogo reaccionarADesahogo;

    public DesahogoController(PublicarDesahogo publicarDesahogo,
                              ListarDesahogos listarDesahogos,
                              ObservarDesahogos observarDesahogos,
                              ReaccionarADesahogo reaccionarADesahogo) {
        this.publicarDesahogo = publicarDesahogo;
        this.listarDesahogos = listarDesahogos;
        this.observarDesahogos = observarDesahogos;
        this.reaccionarADesahogo = reaccionarADesahogo;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PublicacionResponse> publicar(@RequestBody PublicarDesahogoRequest publicarDesahogoRequest) {

        return publicarDesahogo
                .ejecutar(publicarDesahogoRequest.mensaje(),
                        publicarDesahogoRequest.diasDesempleado())
                .map(PublicacionResponse::desde);
    }

    @GetMapping
    public Flux<PublicacionResponse> listar() {
        return listarDesahogos
                .ejecutar()
                .map(PublicacionResponse::desde);
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PublicacionResponse> escucharEnVivo() {
        return observarDesahogos.ejecutar()
                .map(PublicacionResponse::desde);
    }

    @PostMapping("/{id}/reacciones")
    public Mono<PublicacionResponse> reaccionar(@PathVariable UUID id,
                                                @RequestBody ReaccionarRequest reaccionarRequest) {
        return reaccionarADesahogo.ejecutar(id, reaccionarRequest.tipoReaccion())
                .map(PublicacionResponse::desde);
    }
}
