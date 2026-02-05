package com.flowpay.atendimento_distribuicao.controller;

import com.flowpay.atendimento_distribuicao.dto.Atendimento;
import com.flowpay.atendimento_distribuicao.service.AtendimentoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin
public class AtendimentoController {


    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping
    public Atendimento criar(@RequestBody Atendimento atendimento) {
        return service.criarAtendimento(atendimento);
    }

    @PostMapping("/{id}/finalizar")
    public void finalizar(@PathVariable Long id) {
        service.finalizarAtendimento(id);
    }

    @GetMapping
    public List<Atendimento> dashboard() {
        return service.getDashboard();
    }
}
