package com.laerson.algamoney.service;

import com.laerson.algamoney.model.Pessoa;
import com.laerson.algamoney.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaRepository.findById(codigo)
                .orElseThrow(() -> new EmptyResultDataAccessException(1)); //Resultado < 1 lança exceção
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        pessoaRepository.save(pessoaSalva);
        return pessoaSalva;
    }

}
