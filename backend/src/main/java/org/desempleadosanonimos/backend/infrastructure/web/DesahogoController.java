package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.application.usecase.ListarDesahogos;
import org.desempleadosanonimos.backend.application.usecase.PublicarDesahogo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/desahogos")
public class DesahogoController {

    private final PublicarDesahogo publicarDesahogo;
    private final ListarDesahogos listarDesahogos;

    public DesahogoController(PublicarDesahogo publicarDesahogo,
                              ListarDesahogos listarDesahogos) {
        this.publicarDesahogo = publicarDesahogo;
        this.listarDesahogos = listarDesahogos;
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
}
