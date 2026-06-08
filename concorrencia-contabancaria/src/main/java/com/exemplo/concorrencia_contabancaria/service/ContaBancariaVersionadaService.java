package com.exemplo.concorrencia_contabancaria.service;

import com.exemplo.concorrencia_contabancaria.model.ContaBancariaVersionada;
import com.exemplo.concorrencia_contabancaria.repository.ContaBancariaVersionadaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class ContaBancariaVersionadaService {

    private final ContaBancariaVersionadaRepository repository;

    public ContaBancariaVersionadaService(ContaBancariaVersionadaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContaBancariaVersionada depositar(Long id, BigDecimal valor) {
        ContaBancariaVersionada conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta versionada não encontrada"));

        conta.setSaldo(conta.getSaldo().add(valor));
        return repository.save(conta); 
    }

    @Transactional
    public ContaBancariaVersionada sacar(Long id, BigDecimal valor) {
        ContaBancariaVersionada conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta versionada não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        return repository.save(conta);
    }
}