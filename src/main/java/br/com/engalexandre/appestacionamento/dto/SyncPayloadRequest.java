package br.com.engalexandre.appestacionamento.dto;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class SyncPayloadRequest {

    @NotBlank
    private String deviceName;

    @NotBlank
    private String snapshotHash;

    private String appVersion;
    private Instant createdAt;
    private JsonNode vagas;
    private JsonNode convenios;
    private JsonNode movimentacoes;
    private JsonNode produtos;
    private JsonNode vendas;
    private JsonNode servicos;
    private JsonNode mensalistas;
    private JsonNode metadata;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSnapshotHash() {
        return snapshotHash;
    }

    public void setSnapshotHash(String snapshotHash) {
        this.snapshotHash = snapshotHash;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public JsonNode getVagas() {
        return vagas;
    }

    public void setVagas(JsonNode vagas) {
        this.vagas = vagas;
    }

    public JsonNode getConvenios() {
        return convenios;
    }

    public void setConvenios(JsonNode convenios) {
        this.convenios = convenios;
    }

    public JsonNode getMovimentacoes() {
        return movimentacoes;
    }

    public void setMovimentacoes(JsonNode movimentacoes) {
        this.movimentacoes = movimentacoes;
    }

    public JsonNode getProdutos() {
        return produtos;
    }

    public void setProdutos(JsonNode produtos) {
        this.produtos = produtos;
    }

    public JsonNode getVendas() {
        return vendas;
    }

    public void setVendas(JsonNode vendas) {
        this.vendas = vendas;
    }

    public JsonNode getServicos() {
        return servicos;
    }

    public void setServicos(JsonNode servicos) {
        this.servicos = servicos;
    }

    public JsonNode getMensalistas() {
        return mensalistas;
    }

    public void setMensalistas(JsonNode mensalistas) {
        this.mensalistas = mensalistas;
    }

    public JsonNode getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonNode metadata) {
        this.metadata = metadata;
    }
}
