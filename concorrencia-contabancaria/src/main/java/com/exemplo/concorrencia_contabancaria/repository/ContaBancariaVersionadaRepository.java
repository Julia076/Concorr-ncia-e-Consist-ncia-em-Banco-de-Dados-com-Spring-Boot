package com.exemplo.concorrencia_contabancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exemplo.concorrencia_contabancaria.model.ContaBancariaVersionada;

@Repository
public interface ContaBancariaVersionadaRepository extends JpaRepository<ContaBancariaVersionada, Long> {
}