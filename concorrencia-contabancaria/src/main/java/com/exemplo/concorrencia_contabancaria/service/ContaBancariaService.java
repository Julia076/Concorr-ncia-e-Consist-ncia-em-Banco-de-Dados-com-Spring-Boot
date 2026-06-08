package com.exemplo.concorrencia_contabancaria.service;

import com.exemplo.concorrencia_contabancaria.model.ContaBancaria;
import com.exemplo.concorrencia_contabancaria.repository.ContaBancariaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class ContaBancariaService {

    private final ContaBancariaRepository repository;

    public ContaBancariaService(ContaBancariaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ContaBancaria depositar(Long id, BigDecimal valor) {
        ContaBancaria conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        conta.setSaldo(conta.getSaldo().add(valor));
        return repository.save(conta);
    }

    @Transactional
    public ContaBancaria sacar(Long id, BigDecimal valor) {
        ContaBancaria conta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        return repository.save(conta);
    }
}