package com.itau.api_transferencia_bancaria.controller;

import com.itau.api_transferencia_bancaria.dto.TransferenciaDTO;
import com.itau.api_transferencia_bancaria.dto.TransferenciaRequestDTO;
import com.itau.api_transferencia_bancaria.repository.TransferenciaRepository;
import com.itau.api_transferencia_bancaria.service.TransferenciaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/transferencia")
public class TransferenciaController {
    private final TransferenciaRepository repository;
    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaRepository repository, TransferenciaService transferenciaService) {
        this.repository = repository;
        this.transferenciaService = transferenciaService;
    }

    @GetMapping("/numeroConta/{numeroConta}")
    public List<TransferenciaDTO> historicoTransferencia(@PathVariable("numeroConta") String numeroConta) {
        return repository.findAllByClienteOrigemOrClienteDestinoOrderByDataDesc(numeroConta).stream().map(TransferenciaDTO::new).collect(Collectors.toList());
    }

    @PostMapping
    public TransferenciaDTO transferir(@Valid @RequestBody TransferenciaRequestDTO transferenciaRequestDTO){
        return transferenciaService.transferir(transferenciaRequestDTO);
    }
}