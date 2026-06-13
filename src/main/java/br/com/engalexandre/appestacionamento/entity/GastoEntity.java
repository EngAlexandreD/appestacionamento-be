package br.com.engalexandre.appestacionamento.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "gastos")
public class GastoEntity extends BaseEntity {

    @Column(name = "external_id", nullable = false, unique = true, length = 120)
    private String externalId;

    @Column(name = "descricao", nullable = false, length = 512)
    private String descricao;

    @Column(name = "categoria", nullable = false, length = 80)
    private String categoria;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Column(name = "observacao", length = 512)
    private String observacao;

    @Column(name = "data_hora", nullable = false)
    private Instant dataHora;

    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public Instant getDataHora() { return dataHora; }
    public void setDataHora(Instant dataHora) { this.dataHora = dataHora; }
}
