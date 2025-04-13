package com.itau.api_transferencia_bancaria.service;

import com.itau.api_transferencia_bancaria.dto.ClienteDetalhadoDTO;
import com.itau.api_transferencia_bancaria.dto.ClienteResumoDTO;
import com.itau.api_transferencia_bancaria.exception.ClienteNaoEncontradoException;
import com.itau.api_transferencia_bancaria.exception.ContaJaExisteException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public List<ClienteResumoDTO> listarClientes() {
        return repository.findAll().stream().map(ClienteResumoDTO::new).collect(Collectors.toList());
    }

    public ClienteDetalhadoDTO pesquisarCliente(String numeroConta) {
        Cliente cliente = repository.findByNumeroConta(numeroConta);
        if (cliente == null) {
            throw new ClienteNaoEncontradoException("Cliente com número de conta " + numeroConta + " não foi encontrado.");
        }
        return new ClienteDetalhadoDTO(cliente);
    }

    public ClienteDetalhadoDTO criarCliente(ClienteDetalhadoDTO clienteDTO) {

        Cliente clienteExistente = repository.findByNumeroConta(clienteDTO.getNumeroConta());
        if (clienteExistente != null) {
            throw new ContaJaExisteException("Já existe um cliente com o número de conta " + clienteDTO.getNumeroConta() + ".");
        }

        Cliente salvo = repository.save(new Cliente(clienteDTO));
        return new ClienteDetalhadoDTO(salvo);
    }

    @Transactional
    public boolean atualizarSaldo(Cliente clienteOrigem, Cliente clienteDestino, Double valor){
        try {
            repository.subtrairSaldo(clienteOrigem.getId(), valor);
            repository.somarSaldo(clienteDestino.getId(), valor);
            return true;
        }
        catch (Exception e){
            log.error(e.getLocalizedMessage());
            return false;
        }
    }

}
