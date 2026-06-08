package com.exemplo.concorrencia_contabancaria.controller;

import com.exemplo.concorrencia_contabancaria.model.ContaBancariaVersionada;
import com.exemplo.concorrencia_contabancaria.service.ContaBancariaVersionadaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/contas-versionadas")
public class ContaBancariaVersionadaController {

    private final ContaBancariaVersionadaService service;

    public ContaBancariaVersionadaController(ContaBancariaVersionadaService service) {
        this.service = service;
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<ContaBancariaVersionada> deposito(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal valor = request.get("valor");
        return ResponseEntity.ok(service.depositar(id, valor));
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<ContaBancariaVersionada> saque(@PathVariable Long id, @RequestBody Map<String, BigDecimal> request) {
        BigDecimal valor = request.get("valor");
        return ResponseEntity.ok(service.sacar(id, valor));
    }
}