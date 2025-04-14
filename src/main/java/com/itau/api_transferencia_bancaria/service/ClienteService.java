package com.itau.api_transferencia_bancaria.service;

import com.itau.api_transferencia_bancaria.dto.ClienteDetalhadoDTO;
import com.itau.api_transferencia_bancaria.dto.ClienteResumoDTO;
import com.itau.api_transferencia_bancaria.exception.ClienteNaoEncontradoException;
import com.itau.api_transferencia_bancaria.exception.ConcorrenciaException;
import com.itau.api_transferencia_bancaria.exception.ContaJaExisteException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável por gerenciar operações relacionadas aos clientes bancários,
 * como listagem, criação, pesquisa e atualização de saldo entre contas.
 * Aplica regras de negócio e trata exceções específicas para cada operação.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    /**
     * Lista todos os clientes cadastrados no sistema com informações resumidas.
     *
     * @return lista de {@link ClienteResumoDTO} representando os clientes.
     */
    public List<ClienteResumoDTO> listarClientes() {
        return repository.findAll().stream().map(ClienteResumoDTO::new).collect(Collectors.toList());
    }

    /**
     * Pesquisa um cliente com base no número da conta.
     *
     * @param numeroConta o número da conta do cliente.
     * @return os dados detalhados do cliente em {@link ClienteDetalhadoDTO}.
     * @throws ClienteNaoEncontradoException se nenhum cliente for encontrado.
     */
    public ClienteDetalhadoDTO pesquisarCliente(String numeroConta) {
        Cliente cliente = repository.findByNumeroConta(numeroConta);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com número de conta " + numeroConta + " não foi encontrado.");
        }
        return new ClienteDetalhadoDTO(cliente);
    }

    /**
     * Cria um novo cliente, verificando se a conta já existe previamente.
     *
     * @param clienteDTO os dados do cliente a ser criado.
     * @return os dados detalhados do cliente criado.
     * @throws ContaJaExisteException se já houver uma conta com o mesmo número.
     */
    public ClienteDetalhadoDTO criarCliente(ClienteDetalhadoDTO clienteDTO) {

        Cliente clienteExistente = repository.findByNumeroConta(clienteDTO.getNumeroConta());
        if (clienteExistente != null) {
            throw new ContaJaExisteException("Já existe um cliente com o número de conta " + clienteDTO.getNumeroConta() + ".");
        }

        Cliente salvo = repository.save(new Cliente(clienteDTO));
        return new ClienteDetalhadoDTO(salvo);
    }

    /**
     * Atualiza o saldo entre duas contas de clientes, debitando da origem e creditando no destino.
     * Utiliza controle de concorrência otimista via @Version.
     *
     * @param clienteOrigem o cliente de onde será debitado o valor.
     * @param clienteDestino o cliente que receberá o valor.
     * @param valor o valor a ser transferido.
     * @return {@code true} se a operação foi realizada com sucesso; {@code false} em caso de erro genérico.
     * @throws ConcorrenciaException se ocorrer conflito de versão (concorrência) ao salvar os dados.
     */
    @Transactional
    public boolean atualizarSaldo(Cliente clienteOrigem, Cliente clienteDestino, Double valor){
        clienteOrigem.setSaldoConta(clienteOrigem.getSaldoConta() - valor);
        clienteDestino.setSaldoConta(clienteDestino.getSaldoConta() + valor);

        try {
            repository.save(clienteOrigem);
            repository.save(clienteDestino);
            return true;
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error(e.getMessage());
            throw new ConcorrenciaException("Falha de concorrência: a conta foi modificada por outra operação.");
        }
        catch (Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

}
