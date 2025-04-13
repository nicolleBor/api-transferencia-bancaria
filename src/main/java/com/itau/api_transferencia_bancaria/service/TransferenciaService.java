package com.itau.api_transferencia_bancaria.service;

import com.itau.api_transferencia_bancaria.dto.EnumStatusTransferencia;
import com.itau.api_transferencia_bancaria.dto.TransferenciaDTO;
import com.itau.api_transferencia_bancaria.dto.TransferenciaRequestDTO;
import com.itau.api_transferencia_bancaria.exception.OperacaoNaoPermitidaException;
import com.itau.api_transferencia_bancaria.exception.SaldoInsuficienteException;
import com.itau.api_transferencia_bancaria.exception.TransferenciaInvalidaException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.model.Transferencia;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import com.itau.api_transferencia_bancaria.repository.TransferenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TransferenciaService {

    private final TransferenciaRepository repository;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;

    public TransferenciaService(TransferenciaRepository repository, ClienteRepository transferenciaService, ClienteService clienteService) {
        this.repository = repository;
        this.clienteRepository = transferenciaService;
        this.clienteService = clienteService;
    }

    public TransferenciaDTO transferir(TransferenciaRequestDTO transferenciaRequestDTO){
        Cliente clienteOrigem = clienteRepository.findByNumeroConta(transferenciaRequestDTO.getContaOrigem());
        Cliente clienteDestino = clienteRepository.findByNumeroConta(transferenciaRequestDTO.getContaDestino());
        EnumStatusTransferencia status = EnumStatusTransferencia.SUCESSO;
        Transferencia transferencia;

        try {
            // Verifica se é da mesma conta
            if (transferenciaRequestDTO.getContaOrigem().equals(transferenciaRequestDTO.getContaDestino())) {
                status = EnumStatusTransferencia.MESMA_CONTA;
                transferencia = new Transferencia(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor(), status);
                repository.save(transferencia);
                throw new TransferenciaInvalidaException("Transação Inválida: transferência entre mesma conta.");
            }

            // Verifica limite do valor
            if (transferenciaRequestDTO.getValor() > 100) {
                status = EnumStatusTransferencia.VALOR_EXCEDENTE;
                transferencia = new Transferencia(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor(), status);
                repository.save(transferencia);
                throw new OperacaoNaoPermitidaException("Operação Não Permitida: valor excede o limite de 100 reais por transferência.");
            }

            // Verifica saldo
            else if (clienteOrigem.getSaldoConta() < transferenciaRequestDTO.getValor()) {
                status = EnumStatusTransferencia.SALDO_INSUFICIENTE;
                transferencia = new Transferencia(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor(), status);
                repository.save(transferencia);
                throw new SaldoInsuficienteException("Saldo insuficiente para transferência solicitada.");
            }

            // Tenta atualizar saldo
            else if (!clienteService.atualizarSaldo(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor())) {
                status = EnumStatusTransferencia.ERRO;
            }
        } catch (RuntimeException e){
            throw e;
        }

        // Salva a transferência bem-sucedida ou erro inesperado
        transferencia = new Transferencia(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor(), status);
        repository.save(transferencia);

        return new TransferenciaDTO(transferencia);
    }
}

