package com.itau.api_transferencia_bancaria.controller;

import com.itau.api_transferencia_bancaria.dto.ClienteDetalhadoDTO;
import com.itau.api_transferencia_bancaria.dto.ClienteResumoDTO;
import com.itau.api_transferencia_bancaria.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClienteResumoDTO>> listarClientes(){
        List<ClienteResumoDTO> clientes = service.listarClientes();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/numeroConta/{numeroConta}")
    public ResponseEntity<ClienteDetalhadoDTO> pesquisarCliente(@PathVariable("numeroConta") String numeroConta){

        ClienteDetalhadoDTO cliente = service.pesquisarCliente(numeroConta);
        return ResponseEntity.ok(cliente);

    }

    @PostMapping
    public ResponseEntity<ClienteDetalhadoDTO> criarCliente(@Valid @RequestBody ClienteDetalhadoDTO cliente){
        ClienteDetalhadoDTO clienteCriado = service.criarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCriado);
    }
}

