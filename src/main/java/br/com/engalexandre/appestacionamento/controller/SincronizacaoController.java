package br.com.engalexandre.appestacionamento.controller;

import br.com.engalexandre.appestacionamento.dto.RequisicaoPayloadSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaLoteSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaRelatorioMensalSincronizacao;
import br.com.engalexandre.appestacionamento.dto.RespostaRestauracaoSnapshot;
import br.com.engalexandre.appestacionamento.dto.RespostaSaudeServidor;
import br.com.engalexandre.appestacionamento.service.SincronizacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/sync")
public class SincronizacaoController {

    private final SincronizacaoService sincronizacaoService;

    public SincronizacaoController(SincronizacaoService sincronizacaoService) {
        this.sincronizacaoService = sincronizacaoService;
    }

    // Endpoint publico usado pelo app para verificar disponibilidade do servidor.
    @GetMapping("/health")
    public RespostaSaudeServidor saude() {
        return new RespostaSaudeServidor("UP", Instant.now());
    }

    // Recebe um snapshot completo do aplicativo e registra o lote no banco.
    @PostMapping("/batches")
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaLoteSincronizacao criarLote(@Valid @RequestBody RequisicaoPayloadSincronizacao requisicao) {
        return sincronizacaoService.armazenarSnapshot(requisicao);
    }

    // Consulta o lote mais recente de um dispositivo especifico.
    @GetMapping("/batches/latest")
    public RespostaLoteSincronizacao buscarUltimoPorDispositivo(@RequestParam String deviceName) {
        return sincronizacaoService.buscarUltimoPorDispositivo(deviceName);
    }

    // Consolida os lotes sincronizados no mes para facilitar auditoria operacional.
    @GetMapping("/reports/monthly")
    public RespostaRelatorioMensalSincronizacao gerarRelatorioMensal(
            @RequestParam int year,
            @RequestParam int month
    ) {
        return sincronizacaoService.montarRelatorioMensal(year, month);
    }

    // Retorna o ultimo snapshot persistido, global ou filtrado por dispositivo.
    @GetMapping("/restauracao/ultimo")
    public RespostaRestauracaoSnapshot restaurarUltimoSnapshot(
            @RequestParam(required = false) String deviceName
    ) {
        return sincronizacaoService.restaurarUltimoSnapshot(deviceName);
    }
}