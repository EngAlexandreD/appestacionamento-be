package br.com.engalexandre.appestacionamento.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "movimentacoes")
public class MovimentacaoEntity extends BaseEntity {

    @Column(name = "operacao_origem_id", unique = true, length = 120)
    private String operacaoOrigemId;

    @Column(name = "ticket_id", unique = true, length = 80, nullable = false)
    private String ticketId;

    @Column(name = "ticket_code", length = 80)
    private String ticketCode;

    @Column(name = "data_hora", nullable = false)
    private Instant dataHora;

    @Column(name = "entrada_em")
    private Instant entrada;

    @Column(name = "convenio", nullable = false, length = 160)
    private String convenio;

    @Column(name = "regra_aplicada", nullable = false, length = 120)
    private String regra;

    @Column(name = "valor_cliente", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorCliente;

    @Column(name = "valor_lojista", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorLojista;

    @Column(name = "metodo_pagamento", nullable = false, length = 40)
    private String metodoPagamento;

    @Column(name = "info_aparelho", nullable = false, length = 160)
    private String infoAparelho;

    @Column(name = "placa", length = 20)
    private String placa;

    @Column(name = "numero_vaga")
    private Integer numeroVaga;

    @Column(name = "minutos_brutos")
    private Integer minutosBrutos;

    @Column(name = "foi_fechado", nullable = false)
    private boolean foiFechado;

    public String getOperacaoOrigemId() {
        return operacaoOrigemId;
    }

    public void setOperacaoOrigemId(String operacaoOrigemId) {
        this.operacaoOrigemId = operacaoOrigemId;
    }

    public Instant getDataHora() {
        return dataHora;
    }

    public void setDataHora(Instant dataHora) {
        this.dataHora = dataHora;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getRegra() {
        return regra;
    }

    public void setRegra(String regra) {
        this.regra = regra;
    }

    public BigDecimal getValorCliente() {
        return valorCliente;
    }

    public void setValorCliente(BigDecimal valorCliente) {
        this.valorCliente = valorCliente;
    }

    public BigDecimal getValorLojista() {
        return valorLojista;
    }

    public void setValorLojista(BigDecimal valorLojista) {
        this.valorLojista = valorLojista;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getInfoAparelho() {
        return infoAparelho;
    }

    public void setInfoAparelho(String infoAparelho) {
        this.infoAparelho = infoAparelho;
    }

    public Instant getEntrada() {
        return entrada;
    }

    public void setEntrada(Instant entrada) {
        this.entrada = entrada;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Integer getNumeroVaga() {
        return numeroVaga;
    }

    public void setNumeroVaga(Integer numeroVaga) {
        this.numeroVaga = numeroVaga;
    }

    public Integer getMinutosBrutos() {
        return minutosBrutos;
    }

    public void setMinutosBrutos(Integer minutosBrutos) {
        this.minutosBrutos = minutosBrutos;
    }

    public boolean isFoiFechado() {
        return foiFechado;
    }

    public void setFoiFechado(boolean foiFechado) {
        this.foiFechado = foiFechado;
    }
}