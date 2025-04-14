package com.itau.api_transferencia_bancaria.service;

import com.itau.api_transferencia_bancaria.dto.EnumStatusTransferencia;
import com.itau.api_transferencia_bancaria.dto.TransferenciaDTO;
import com.itau.api_transferencia_bancaria.dto.TransferenciaRequestDTO;
import com.itau.api_transferencia_bancaria.exception.ConcorrenciaException;
import com.itau.api_transferencia_bancaria.exception.OperacaoNaoPermitidaException;
import com.itau.api_transferencia_bancaria.exception.SaldoInsuficienteException;
import com.itau.api_transferencia_bancaria.exception.TransferenciaInvalidaException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.model.Transferencia;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import com.itau.api_transferencia_bancaria.repository.TransferenciaRepository;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pelas operações de transferência entre contas bancárias.
 * Valida regras de negócio, atualiza saldos e registra o histórico da transferência.
 */
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

    /**
     * Realiza uma transferência entre contas, aplicando as regras de negócio:
     * <ul>
     *     <li>Não permite transferências entre a mesma conta</li>
     *     <li>Limita o valor máximo a R$100,00</li>
     *     <li>Valida se o saldo da conta de origem é suficiente</li>
     *     <li>Lança exceções apropriadas para cada violação</li>
     * </ul>
     *
     * @param transferenciaRequestDTO dados da transferência
     * @return objeto contendo os detalhes da transferência realizada
     * @throws TransferenciaInvalidaException se a conta de origem for igual à de destino
     * @throws OperacaoNaoPermitidaException se o valor exceder o limite permitido
     * @throws SaldoInsuficienteException se o saldo da conta for insuficiente
     * @throws ConcorrenciaException se ocorrer um conflito de concorrência durante a operação
     */
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

            try {
                // Tenta atualizar saldo
                if (!clienteService.atualizarSaldo(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor())) {
                    status = EnumStatusTransferencia.ERRO;
                }
            } catch (ConcorrenciaException e) {
                status = EnumStatusTransferencia.CONCORRENCIA;
                transferencia = new Transferencia(clienteOrigem, clienteDestino, transferenciaRequestDTO.getValor(), status);
                repository.save(transferencia);
                throw e;
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

