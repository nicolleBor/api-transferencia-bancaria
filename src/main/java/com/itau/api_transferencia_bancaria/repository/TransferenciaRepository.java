package com.itau.api_transferencia_bancaria.repository;

import com.itau.api_transferencia_bancaria.model.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    @Query("SELECT t FROM Transferencia t WHERE t.clienteOrigem.numeroConta = :numeroConta OR t.clienteDestino.numeroConta = :numeroConta ORDER BY t.data DESC")
    List<Transferencia> findAllByClienteOrigemOrClienteDestinoOrderByDataDesc(@Param("numeroConta") String numeroConta);
}
