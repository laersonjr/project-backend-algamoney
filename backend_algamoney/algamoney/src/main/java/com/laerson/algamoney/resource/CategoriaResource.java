package com.laerson.algamoney.resource;

import com.laerson.algamoney.model.Categoria;
import com.laerson.algamoney.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("categorias")
public class CategoriaResource {

    private final CategoriaRepository categoriaRepository;

    public CategoriaResource(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }


    @GetMapping
    public List<Categoria> listar(){
        return categoriaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria){
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(categoriaSalva.getCodigo()).toUri();
        // response.setHeader("Location", uri.toASCIIString());
        return ResponseEntity.created(uri).body(categoriaSalva);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPeloCodigo(@Valid @PathVariable Long codigo){
        return this.categoriaRepository.findById(codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

}
