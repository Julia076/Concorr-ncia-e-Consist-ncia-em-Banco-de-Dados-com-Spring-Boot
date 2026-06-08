package com.exemplo.concorrencia_contabancaria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.math.BigDecimal;

@Entity
public class ContaBancariaVersionada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCliente;
    private BigDecimal saldo;

    @Version
    private Integer version; 

    public ContaBancariaVersionada() {}

    public ContaBancariaVersionada(String nomeCliente, BigDecimal saldo) {
        this.nomeCliente = nomeCliente;
        this.saldo = saldo;
    }

    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public Integer getVersion() { return version; }
}