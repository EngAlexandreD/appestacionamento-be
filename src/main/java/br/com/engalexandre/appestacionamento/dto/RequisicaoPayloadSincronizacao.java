package br.com.engalexandre.appestacionamento.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

// Publica o contrato em portugues e ainda aceita as chaves antigas para migracao segura.
public class RequisicaoPayloadSincronizacao {

    @JsonProperty("nomeDispositivo")
    @JsonAlias("deviceName")
    @NotBlank
    private String nomeDispositivo;

    @JsonProperty("hashSnapshot")
    @JsonAlias("snapshotHash")
    @NotBlank
    private String hashSnapshot;

    @JsonProperty("versaoApp")
    @JsonAlias("appVersion")
    private String versaoApp;

    @JsonProperty("criadoEm")
    @JsonAlias("createdAt")
    private Instant criadoEm;

    private JsonNode vagas;
    private JsonNode convenios;
    private JsonNode movimentacoes;
    private JsonNode produtos;
    private JsonNode vendas;
    private JsonNode servicos;
    private JsonNode mensalistas;
    @JsonProperty("metadados")
    @JsonAlias("metadata")
    private JsonNode metadados;

    @JsonProperty("estadoBruto")
    @JsonAlias("rawState")
    private JsonNode estadoBruto;

    public String getNomeDispositivo() {
        return nomeDispositivo;
    }

    public void setNomeDispositivo(String nomeDispositivo) {
        this.nomeDispositivo = nomeDispositivo;
    }

    public String getHashSnapshot() {
        return hashSnapshot;
    }

    public void setHashSnapshot(String hashSnapshot) {
        this.hashSnapshot = hashSnapshot;
    }

    public String getVersaoApp() {
        return versaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        this.versaoApp = versaoApp;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
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

    public JsonNode getMetadados() {
        return metadados;
    }

    public void setMetadados(JsonNode metadados) {
        this.metadados = metadados;
    }

    public JsonNode getEstadoBruto() {
        return estadoBruto;
    }

    public void setEstadoBruto(JsonNode estadoBruto) {
        this.estadoBruto = estadoBruto;
    }
}