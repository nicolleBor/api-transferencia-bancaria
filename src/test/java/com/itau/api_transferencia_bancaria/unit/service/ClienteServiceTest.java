package com.itau.api_transferencia_bancaria.unit.service;

import com.itau.api_transferencia_bancaria.dto.ClienteDetalhadoDTO;
import com.itau.api_transferencia_bancaria.dto.ClienteResumoDTO;
import com.itau.api_transferencia_bancaria.exception.ClienteNaoEncontradoException;
import com.itau.api_transferencia_bancaria.exception.ContaJaExisteException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import com.itau.api_transferencia_bancaria.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    void listarClientes_deveRetornarListaDeResumoDTO() {
        Cliente cliente = new Cliente(1L, "João", "123", 50.0);
        Mockito.when(repository.findAll()).thenReturn(List.of(cliente));

        List<ClienteResumoDTO> resultado = service.listarClientes();

        assertEquals(1, resultado.size());
        assertEquals("João", resultado.get(0).getNome());
    }

    @Test
    void pesquisarCliente_clienteExiste_deveRetornarDetalhadoDTO() {
        Cliente cliente = new Cliente(1L, "João", "123", 50.0);
        Mockito.when(repository.findByNumeroConta("123")).thenReturn(cliente);

        ClienteDetalhadoDTO resultado = service.pesquisarCliente("123");

        assertEquals("João", resultado.getNome());
    }

    @Test
    void pesquisarCliente_clienteNaoExiste_deveLancarExcecao() {
        Mockito.when(repository.findByNumeroConta("123")).thenReturn(null);

        assertThrows(ClienteNaoEncontradoException.class, () -> service.pesquisarCliente("123"));
    }

    @Test
    void criarCliente_comNumeroContaExistente_deveLancarExcecao() {
        ClienteDetalhadoDTO dto = new ClienteDetalhadoDTO();
        dto.setNumeroConta("123");
        Mockito.when(repository.findByNumeroConta("123")).thenReturn(new Cliente());

        assertThrows(ContaJaExisteException.class, () -> service.criarCliente(dto));
    }
}
