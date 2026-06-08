package com.exemplo.concorrencia_contabancaria.controller;

import com.exemplo.concorrencia_contabancaria.model.ContaBancaria;
import com.exemplo.concorrencia_contabancaria.service.ContaBancariaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/contas")
public class ContaBancariaController {

    private final ContaBancariaService service;

    public ContaBancariaController(ContaBancariaService service) {
        this.service = service;
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<ContaBancaria> deposito(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal valor = request.get("valor");
        return ResponseEntity.ok(service.depositar(id, valor));
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<ContaBancaria> saque(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal valor = request.get("valor");
        return ResponseEntity.ok(service.sacar(id, valor));
    }
}