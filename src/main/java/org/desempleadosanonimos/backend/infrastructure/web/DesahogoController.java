package org.desempleadosanonimos.backend.infrastructure.web;

import org.desempleadosanonimos.backend.aplication.usecase.ListarDeshagos;
import org.desempleadosanonimos.backend.aplication.usecase.PublicarDeshago;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/desahogos")
public class DesahogoController {

    private final PublicarDeshago publicarDeshago;
    private final ListarDeshagos listarDeshagos;

    public DesahogoController(PublicarDeshago publicarDeshago,
                              ListarDeshagos listarDeshagos) {
        this.publicarDeshago = publicarDeshago;
        this.listarDeshagos = listarDeshagos;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PublicacionResponse> publicar(@RequestBody PublicarDesahogoRequest publicarDesahogoRequest) {

        return publicarDeshago
                .ejecutar(publicarDesahogoRequest.mensaje(),
                        publicarDesahogoRequest.diasDesempleado())
                .map(PublicacionResponse::desde);
    }

    @GetMapping
    public Flux<PublicacionResponse> listar() {
        return listarDeshagos
                .ejecutar()
                .map(PublicacionResponse::desde);
    }
}
