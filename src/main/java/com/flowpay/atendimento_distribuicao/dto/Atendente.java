package com.flowpay.atendimento_distribuicao.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Atendente {
    private Long id;
    private String nome;
    private TipoAssunto time;
    private List<Atendimento> atendimentosAtivos = new ArrayList<>();

    public Atendente(Long id, String nome, TipoAssunto time) {
        this.id = id;
        this.nome = nome;
        this.time = time;
    }
}
