package br.com.engalexandre.appestacionamento.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "servico_vendas")
public class ServicoVendaEntity extends BaseEntity {

    @Column(name = "external_id", nullable = false, unique = true, length = 120)
    private String externalId;

    @Column(name = "servico_external_id", length = 120)
    private String servicoExternalId;

    @Column(name = "nome", nullable = false, length = 160)
    private String nome;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "metodo", nullable = false, length = 40)
    private String metodo;

    @Column(name = "data_hora", nullable = false)
    private Instant data;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getServicoExternalId() {
        return servicoExternalId;
    }

    public void setServicoExternalId(String servicoExternalId) {
        this.servicoExternalId = servicoExternalId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }
}