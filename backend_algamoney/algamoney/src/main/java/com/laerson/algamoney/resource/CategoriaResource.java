package com.laerson.algamoney.resource;

import com.laerson.algamoney.event.RecursoCriadoEvent;
import com.laerson.algamoney.model.Categoria;
import com.laerson.algamoney.repository.CategoriaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("categorias")
public class CategoriaResource {

    private final CategoriaRepository categoriaRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CategoriaResource(CategoriaRepository categoriaRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.categoriaRepository = categoriaRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@Valid @PathVariable Long codigo) {
        return this.categoriaRepository.findById(codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
