package com.itau.api_transferencia_bancaria.repository;

import com.itau.api_transferencia_bancaria.model.Cliente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByNumeroConta(String numeroConta);

    boolean existsByNumeroConta(String numeroConta);

    //Controle de Concorrência
    @Query(value = "SELECT c.* FROM CLIENTE c WHERE c.NUMERO_CONTA = :numeroConta for update nowait", nativeQuery = true)
    @Transactional
    Cliente findByNumeroContaComLock(@Param("numeroConta") String numeroConta);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.saldoConta = c.saldoConta - :valor WHERE c.id = :clienteId AND c.saldoConta >= :valor")
    void subtrairSaldo(@Param("clienteId") Long clienteId, @Param("valor") Double valor);

    @Transactional
    @Modifying
    @Query("UPDATE Cliente c SET c.saldoConta = c.saldoConta + :valor WHERE c.id = :clienteId")
    void somarSaldo(@Param("clienteId") Long clienteId, @Param("valor") Double valor);
}
