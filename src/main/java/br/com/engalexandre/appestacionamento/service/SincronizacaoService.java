package br.com.engalexandre.appestacionamento.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.engalexandre.appestacionamento.dto.RequisicaoPayloadSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaLoteSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaRelatorioMensalSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaRestauracaoSnapshot;
import br.com.engalexandre.appestacionamento.model.LoteSincronizacao;
import br.com.engalexandre.appestacionamento.repository.LoteSincronizacaoRepository;

@Service
public class SincronizacaoService {

    private final LoteSincronizacaoRepository repositorio;
    private final ObjectMapper objectMapper;

    public SincronizacaoService(LoteSincronizacaoRepository repositorio, ObjectMapper objectMapper) {
        this.repositorio = repositorio;
        this.objectMapper = objectMapper;
    }

    // Evita duplicidade reaproveitando o mesmo snapshot quando o hash ja existe para o dispositivo.
    public RespostaLoteSincronizacao armazenarSnapshot(RequisicaoPayloadSincronizacao requisicao) {
        return repositorio.findByDeviceNameAndSnapshotHash(requisicao.getNomeDispositivo(), requisicao.getHashSnapshot())
                .map(loteExistente -> paraResposta(loteExistente, true))
                .orElseGet(() -> persistirNovoLote(requisicao));
    }

    // Recupera o lote mais recente para um dispositivo especifico.
    public RespostaLoteSincronizacao buscarUltimoPorDispositivo(String nomeDispositivo) {
        LoteSincronizacao lote = repositorio.findTopByDeviceNameOrderBySynchronizedAtDesc(nomeDispositivo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhuma sincronizacao encontrada para o dispositivo."));
        return paraResposta(lote, false);
    }

    // Consolida o volume sincronizado por dispositivo dentro da competencia informada.
    public RespostaRelatorioMensalSincronizacao montarRelatorioMensal(int ano, int mes) {
        if (mes < 1 || mes > 12) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mes deve estar entre 1 e 12.");
        }

        ZoneId fusoHorario = ZoneId.of("America/Sao_Paulo");
        Instant inicio = LocalDateTime.of(ano, mes, 1, 0, 0).atZone(fusoHorario).toInstant();
        Instant fim = LocalDateTime.of(ano, mes, 1, 0, 0).plusMonths(1).atZone(fusoHorario).toInstant();

        List<LoteSincronizacao> lotes = repositorio.findBySynchronizedAtBetweenOrderBySynchronizedAtDesc(inicio, fim);
        Map<String, List<LoteSincronizacao>> lotesPorDispositivo = lotes.stream()
                .collect(Collectors.groupingBy(LoteSincronizacao::getDeviceName));

        List<RespostaRelatorioMensalSincronizacao.ResumoSincronizacaoDispositivo> dispositivos = lotesPorDispositivo
                .entrySet()
                .stream()
                .map(entrada -> {
                    long totalRegistros = entrada.getValue().stream().mapToLong(LoteSincronizacao::getTotalRecords).sum();
                    Instant ultimaSincronizacao = entrada.getValue().stream()
                            .map(LoteSincronizacao::getSynchronizedAt)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    return new RespostaRelatorioMensalSincronizacao.ResumoSincronizacaoDispositivo(
                            entrada.getKey(),
                            entrada.getValue().size(),
                            totalRegistros,
                            ultimaSincronizacao
                    );
                })
                .sorted(Comparator.comparing(RespostaRelatorioMensalSincronizacao.ResumoSincronizacaoDispositivo::nomeDispositivo))
                .toList();

        Instant ultimaSincronizacao = lotes.stream()
                .map(LoteSincronizacao::getSynchronizedAt)
                .max(Comparator.naturalOrder())
                .orElse(null);

        long totalRegistros = lotes.stream().mapToLong(LoteSincronizacao::getTotalRecords).sum();

        return new RespostaRelatorioMensalSincronizacao(
                ano,
                mes,
                lotes.size(),
                dispositivos.size(),
                totalRegistros,
                ultimaSincronizacao,
                dispositivos
        );
    }

    // Reconstroi o ultimo snapshot salvo para apoiar restauracao operacional do app.
    public RespostaRestauracaoSnapshot restaurarUltimoSnapshot(String nomeDispositivo) {
        LoteSincronizacao lote = buscarUltimoLote(nomeDispositivo);
        return new RespostaRestauracaoSnapshot(
                lote.getId(),
                lote.getDeviceName(),
                lote.getSnapshotHash(),
                lote.getAppVersion(),
                lote.getCreatedAt(),
                lote.getSynchronizedAt(),
                lote.getTotalRecords(),
                desserializarPayload(lote.getPayloadJson())
        );
    }

    // Persiste um novo lote mantendo os metadados necessarios para auditoria e restauracao.
    private RespostaLoteSincronizacao persistirNovoLote(RequisicaoPayloadSincronizacao requisicao) {
        LoteSincronizacao lote = new LoteSincronizacao();
        lote.setDeviceName(requisicao.getNomeDispositivo());
        lote.setSnapshotHash(requisicao.getHashSnapshot());
        lote.setAppVersion(requisicao.getVersaoApp());
        lote.setCreatedAt(requisicao.getCriadoEm() == null ? Instant.now() : requisicao.getCriadoEm());
        lote.setSynchronizedAt(Instant.now());
        lote.setTotalRecords(calcularTotalRegistros(requisicao));
        lote.setPayloadJson(serializarPayload(requisicao));

        LoteSincronizacao salvo = repositorio.save(lote);
        return paraResposta(salvo, false);
    }

    // Seleciona o lote mais recente de um dispositivo ou, sem filtro, o mais recente do servidor inteiro.
    private LoteSincronizacao buscarUltimoLote(String nomeDispositivo) {
        if (nomeDispositivo != null && !nomeDispositivo.isBlank()) {
            return repositorio.findTopByDeviceNameOrderBySynchronizedAtDesc(nomeDispositivo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Nenhum snapshot encontrado para o dispositivo informado."));
        }

        return repositorio.findTopByOrderBySynchronizedAtDesc()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Nenhum snapshot foi salvo no servidor ainda."));
    }

    // Conta registros de todas as secoes para gerar uma visao rapida do volume restaurado/sincronizado.
    private int calcularTotalRegistros(RequisicaoPayloadSincronizacao requisicao) {
        return tamanhoDe(requisicao.getVagas())
                + tamanhoDe(requisicao.getConvenios())
                + tamanhoDe(requisicao.getMovimentacoes())
                + tamanhoDe(requisicao.getProdutos())
                + tamanhoDe(requisicao.getVendas())
                + tamanhoDe(requisicao.getServicos())
                + tamanhoDe(requisicao.getMensalistas())
                + tamanhoDe(requisicao.getEstadoBruto());
    }

    // Trata arrays, objetos e valores simples sem depender do tipo original recebido.
    private int tamanhoDe(JsonNode no) {
        if (no == null || no.isNull()) {
            return 0;
        }
        if (no.isArray() || no.isObject()) {
            return no.size();
        }
        return 1;
    }

    // Guarda o payload bruto para permitir restauracao fiel no futuro.
    private String serializarPayload(RequisicaoPayloadSincronizacao requisicao) {
        try {
            return objectMapper.writeValueAsString(requisicao);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nao foi possivel serializar o payload recebido.");
        }
    }

    // Converte o JSON salvo no banco em arvore para devolucao segura na API de restauracao.
    private JsonNode desserializarPayload(String payloadJson) {
        try {
            return objectMapper.readTree(payloadJson);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Nao foi possivel ler o payload salvo para restauracao.");
        }
    }

    // Centraliza a montagem da resposta padrao de lotes para manter consistencia entre endpoints.
    private RespostaLoteSincronizacao paraResposta(LoteSincronizacao lote, boolean duplicado) {
        return new RespostaLoteSincronizacao(
                lote.getId(),
                lote.getDeviceName(),
                lote.getSnapshotHash(),
                duplicado,
                lote.getTotalRecords(),
                lote.getCreatedAt(),
                lote.getSynchronizedAt()
        );
    }
}