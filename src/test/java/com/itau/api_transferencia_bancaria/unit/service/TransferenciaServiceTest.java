package com.itau.api_transferencia_bancaria.unit.service;

import com.itau.api_transferencia_bancaria.dto.EnumStatusTransferencia;
import com.itau.api_transferencia_bancaria.dto.TransferenciaDTO;
import com.itau.api_transferencia_bancaria.dto.TransferenciaRequestDTO;
import com.itau.api_transferencia_bancaria.exception.OperacaoNaoPermitidaException;
import com.itau.api_transferencia_bancaria.exception.SaldoInsuficienteException;
import com.itau.api_transferencia_bancaria.exception.TransferenciaInvalidaException;
import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import com.itau.api_transferencia_bancaria.repository.TransferenciaRepository;
import com.itau.api_transferencia_bancaria.service.ClienteService;
import com.itau.api_transferencia_bancaria.service.TransferenciaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepo;

    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private TransferenciaService service;

    @Test
    void transferir_mesmaConta_deveLancarExcecao() {
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO("123", "123", 50.0);
        Cliente cliente = new Cliente(1L, "João", "123", 100.0);
        Mockito.when(clienteRepo.findByNumeroConta("123")).thenReturn(cliente);

        assertThrows(TransferenciaInvalidaException.class, () -> service.transferir(dto));
    }

    @Test
    void transferir_valorExcedente_deveLancarExcecao() {
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO("123", "456", 200.0);
        Cliente origem = new Cliente(1L, "João", "123", 500.0);
        Cliente destino = new Cliente(2L, "Maria", "456", 100.0);
        Mockito.when(clienteRepo.findByNumeroConta("123")).thenReturn(origem);
        Mockito.when(clienteRepo.findByNumeroConta("456")).thenReturn(destino);

        assertThrows(OperacaoNaoPermitidaException.class, () -> service.transferir(dto));
    }

    @Test
    void transferir_saldoInsuficiente_deveLancarExcecao() {
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO("123", "456", 60.0);
        Cliente origem = new Cliente(1L, "João", "123", 50.0);
        Cliente destino = new Cliente(2L, "Maria", "456", 100.0);
        Mockito.when(clienteRepo.findByNumeroConta("123")).thenReturn(origem);
        Mockito.when(clienteRepo.findByNumeroConta("456")).thenReturn(destino);

        assertThrows(SaldoInsuficienteException.class, () -> service.transferir(dto));
    }

    @Test
    void transferir_sucesso_deveRetornarDTO() {
        TransferenciaRequestDTO dto = new TransferenciaRequestDTO("123", "456", 50.0);
        Cliente origem = new Cliente(1L, "João", "123", 100.0);
        Cliente destino = new Cliente(2L, "Maria", "456", 100.0);

        Mockito.when(clienteRepo.findByNumeroConta("123")).thenReturn(origem);
        Mockito.when(clienteRepo.findByNumeroConta("456")).thenReturn(destino);
        Mockito.when(clienteService.atualizarSaldo(origem, destino, 50.0)).thenReturn(true);

        TransferenciaDTO resultado = service.transferir(dto);

        assertEquals(50.0, resultado.getValor());
        assertEquals(EnumStatusTransferencia.SUCESSO, resultado.getStatus());
    }
}
