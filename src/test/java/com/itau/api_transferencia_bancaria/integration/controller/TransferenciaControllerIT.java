package com.itau.api_transferencia_bancaria.integration.controller;

import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import com.itau.api_transferencia_bancaria.repository.TransferenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TransferenciaControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepo;

    @Autowired
    private TransferenciaRepository transferenciaRepo;

    @BeforeEach
    void setup() {
        transferenciaRepo.deleteAll();
        clienteRepo.deleteAll();

        clienteRepo.save(new Cliente(null, "João", "123", 100.0, null));
        clienteRepo.save(new Cliente(null, "Maria", "456", 50.0, null));
    }

    @Test
    void transferir_retornaDTO() throws Exception {
        String json = """
            {
              "contaOrigem": "123",
              "contaDestino": "456",
              "valor": 30.0
            }
        """;

        mockMvc.perform(post("/api/v1/transferencia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(30.0))
                .andExpect(jsonPath("$.status").value("SUCESSO"));
    }

    @Test
    void historicoTransferencia_retornaTransferencias() throws Exception {
        // primeiro insere uma transferência
        transferir_retornaDTO();

        mockMvc.perform(get("/api/v1/transferencia/numeroConta/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].valor").value(30.0));
    }
}
