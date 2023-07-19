package com.laerson.algamoney.resource;

import com.laerson.algamoney.event.RecursoCriadoEvent;
import com.laerson.algamoney.model.Pessoa;
import com.laerson.algamoney.repository.PessoaRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    private final PessoaRepository pessoaRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public PessoaResource(PessoaRepository pessoaRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.pessoaRepository = pessoaRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPessoaId(@PathVariable Long codigo) {
        return pessoaRepository.findById(codigo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        applicationEventPublisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

}
