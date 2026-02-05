package com.flowpay.atendimento_distribuicao.dto;

import lombok.Data;

@Data
public class Atendimento {
    private Long id;
    private String cliente;
    private TipoAssunto assunto;
    private StatusAtendimento status;
    private Long atendenteId;
}
