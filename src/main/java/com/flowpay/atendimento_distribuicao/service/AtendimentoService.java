package com.flowpay.atendimento_distribuicao.service;

import com.flowpay.atendimento_distribuicao.dto.Atendente;
import com.flowpay.atendimento_distribuicao.dto.Atendimento;
import com.flowpay.atendimento_distribuicao.dto.StatusAtendimento;
import com.flowpay.atendimento_distribuicao.dto.TipoAssunto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AtendimentoService {
    private Map<TipoAssunto, Queue<Atendimento>> filas = new HashMap<>();
    private List<Atendente> atendentes = new ArrayList<>();
    private List<Atendimento> todosAtendimentos = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong();

    public AtendimentoService() {
        filas.put(TipoAssunto.CARTAO, new LinkedList<>());
        filas.put(TipoAssunto.EMPRESTIMO, new LinkedList<>());
        filas.put(TipoAssunto.OUTROS, new LinkedList<>());

        atendentes.add(new Atendente(1L, "Ana", TipoAssunto.CARTAO));
        atendentes.add(new Atendente(2L, "Bruno", TipoAssunto.EMPRESTIMO));
        atendentes.add(new Atendente(3L, "Carla", TipoAssunto.OUTROS));
    }

    public Atendimento criarAtendimento(Atendimento atendimento) {
        atendimento.setId(idGenerator.incrementAndGet());
        atendimento.setStatus(StatusAtendimento.EM_FILA);
        todosAtendimentos.add(atendimento);

        tentarDistribuir(atendimento);
        return atendimento;
    }

    private void tentarDistribuir(Atendimento atendimento) {

        List<Atendente> atendentesDoTime = atendentes.stream()
                .filter(a -> a.getTime() == atendimento.getAssunto())
                .toList();

        Optional<Atendente> atendenteDisponivel = atendentesDoTime.stream()
                .filter(a -> a.getAtendimentosAtivos().size() < 3)
                .min(Comparator.comparingInt(a -> a.getAtendimentosAtivos().size()));

        if (atendenteDisponivel.isPresent()) {
            iniciarAtendimento(atendenteDisponivel.get(), atendimento);
        } else {
            filas.get(atendimento.getAssunto()).offer(atendimento);
        }
    }

    private void iniciarAtendimento(Atendente atendente, Atendimento atendimento) {
        atendimento.setStatus(StatusAtendimento.EM_ATENDIMENTO);
        atendimento.setAtendenteId(atendente.getId());
        atendente.getAtendimentosAtivos().add(atendimento);
    }


    public void finalizarAtendimento(Long id) {

        Atendimento atendimento = todosAtendimentos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado"));

        atendimento.setStatus(StatusAtendimento.FINALIZADO);

        Atendente atendente = atendentes.stream()
                .filter(a -> a.getId().equals(atendimento.getAtendenteId()))
                .findFirst()
                .orElseThrow();

        atendente.getAtendimentosAtivos().removeIf(a -> a.getId().equals(id));

        chamarProximoDaFila(atendimento.getAssunto());
    }

    private void chamarProximoDaFila(TipoAssunto assunto) {
        Queue<Atendimento> fila = filas.get(assunto);

        if (!fila.isEmpty()) {
            Atendimento proximo = fila.poll();
            tentarDistribuir(proximo);
        }
    }

    public List<Atendimento> getDashboard() {
        return todosAtendimentos;
    }


}
