package com.exemplo.concorrencia_contabancaria;


import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exemplo.concorrencia_contabancaria.model.ContaBancaria;
import com.exemplo.concorrencia_contabancaria.model.ContaBancariaVersionada;
import com.exemplo.concorrencia_contabancaria.repository.ContaBancariaRepository;
import com.exemplo.concorrencia_contabancaria.repository.ContaBancariaVersionadaRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ContaBancariaRepository repoComum;
    private final ContaBancariaVersionadaRepository repoVersionado;

    
    public DatabaseLoader(ContaBancariaRepository repoComum, ContaBancariaVersionadaRepository repoVersionado) {
        this.repoComum = repoComum;
        this.repoVersionado = repoVersionado;
    }

    @Override
    public void run(String... args) throws Exception {
        
        repoComum.deleteAll();
        repoVersionado.deleteAll();

        
        ContaBancaria contaComum = new ContaBancaria("Conta A - Sem Trava", new BigDecimal("1000.00"));
        repoComum.save(contaComum);

        
        ContaBancariaVersionada contaVersionada = new ContaBancariaVersionada("Conta B - Protegida", new BigDecimal("1000.00"));
        repoVersionado.save(contaVersionada);

        System.out.println("=================================================");
        System.out.println(" Banco de dados H2 inicializado com sucesso!");
        System.out.println(" Conta 1 configurada para: " + contaComum.getNomeCliente());
        System.out.println(" Conta 2 configurada para: " + contaVersionada.getNomeCliente());
        System.out.println("=================================================");
    }
}