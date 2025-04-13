package com.itau.api_transferencia_bancaria.repository;

import com.itau.api_transferencia_bancaria.model.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByNumeroConta(String numeroConta);

    boolean existsByNumeroConta(String numeroConta);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.saldoConta = c.saldoConta - :valor WHERE c.id = :clienteId AND c.saldoConta >= :valor")
    void subtrairSaldo(@Param("clienteId") Long clienteId, @Param("valor") Double valor);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.saldoConta = c.saldoConta + :valor WHERE c.id = :clienteId")
    void somarSaldo(@Param("clienteId") Long clienteId, @Param("valor") Double valor);
}
