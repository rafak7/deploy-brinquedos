package com.fiap.brinquedos.controller;

import com.fiap.brinquedos.entity.Brinquedo;
import com.fiap.brinquedos.service.BrinquedoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brinquedos")
public class BrinquedoController {

    @Autowired
    private BrinquedoService brinquedoService;

    //GET
    @GetMapping
    public ResponseEntity<List<EntityModel<Brinquedo>>> listar() {
        List<EntityModel<Brinquedo>> brinquedos = brinquedoService.listarTodos().stream()
                .map(brinquedo -> EntityModel.of(brinquedo,
                        createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).consultarId(brinquedo.getIdBrinquedo()), "self"),
                        createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).listar(), "brinquedos")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(brinquedos);
    }

    //POST
    @PostMapping
    public ResponseEntity<EntityModel<Brinquedo>> criarBrinquedo(@RequestBody Brinquedo brinquedo) {
        Brinquedo novoBrinquedo = brinquedoService.salvar(brinquedo);
        EntityModel<Brinquedo> resource = EntityModel.of(novoBrinquedo,
                createLinkWithMethod("POST", WebMvcLinkBuilder.methodOn(BrinquedoController.class).consultarId(novoBrinquedo.getIdBrinquedo()), "self"),
                createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).listar(), "brinquedos"));
        return ResponseEntity.ok(resource);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> atualizarBrinquedo(@PathVariable Long id, @RequestBody Brinquedo brinquedo){
        Brinquedo brinquedoAtualizado = brinquedoService.atualizar(brinquedo, id);
        EntityModel<Brinquedo> resource = EntityModel.of(brinquedoAtualizado,
                createLinkWithMethod("PUT", WebMvcLinkBuilder.methodOn(BrinquedoController.class).atualizarBrinquedo(id, brinquedo), "self"),
                createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).consultarId(id), "consultar"),
                createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).listar(), "brinquedos"));
        return ResponseEntity.ok(resource);
    }

    //GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> consultarId(@PathVariable Long id) {
        Brinquedo brinquedo = brinquedoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Brinquedo Não Encontrado"));
        EntityModel<Brinquedo> resource = EntityModel.of(brinquedo,
                createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).consultarId(brinquedo.getIdBrinquedo()), "self"),
                createLinkWithMethod("GET", WebMvcLinkBuilder.methodOn(BrinquedoController.class).listar(), "brinquedos"));
        return ResponseEntity.ok(resource);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> deletar(@PathVariable Long id) {
        brinquedoService.deletar(id);
        return ResponseEntity.ok().build();
    }

    //OPTIONS
    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options(@PathVariable Long id) {
        boolean brinquedoExistente = brinquedoService.buscarPorId(id).isPresent();
        if (brinquedoExistente) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.ok().allow(HttpMethod.GET, HttpMethod.POST, HttpMethod.PATCH, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD).build();
    }

    //PATH
    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Brinquedo>> atualizarParcialmenteBrinquedo(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Brinquedo brinquedoAtualizado = brinquedoService.atualizarParcialmente(id, updates);

        EntityModel<Brinquedo> resource = EntityModel.of(brinquedoAtualizado,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BrinquedoController.class).consultarId(id)).withRel("consultar"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BrinquedoController.class).listar()).withRel("brinquedos"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BrinquedoController.class).atualizarParcialmenteBrinquedo(id, updates)).withSelfRel());

        return ResponseEntity.ok(resource);
    }

    //HEAD
    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<?> consultarBrinquedoHead(@PathVariable Long id) {
        Optional<Brinquedo> brinquedo = brinquedoService.buscarPorId(id);
        if (!brinquedo.isPresent()) {
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok().build();
        }
    }

    private Link createLinkWithMethod(String method, Object invocationValue, String rel) {
        return Link.of(WebMvcLinkBuilder.linkTo(invocationValue).toUri().toString(), rel)
                .withTitle(method + " method");
    }
}
