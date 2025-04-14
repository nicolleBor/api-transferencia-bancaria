package com.itau.api_transferencia_bancaria.integration.controller;

import com.itau.api_transferencia_bancaria.model.Cliente;
import com.itau.api_transferencia_bancaria.repository.ClienteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new Cliente(null, "João", "123", 100.0, null));
    }

    @Test
    void listarClientes_retorna200() throws Exception {
        mockMvc.perform(get("/api/v1/cliente")).andExpect(status().isOk()).andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void criarCliente_retorna201() throws Exception {
        String json = """
            {
              "nome": "Maria",
              "numeroConta": "456",
              "saldoConta": 200.0
            }
        """;

        mockMvc.perform(post("/api/v1/cliente").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Maria"));
    }
}
