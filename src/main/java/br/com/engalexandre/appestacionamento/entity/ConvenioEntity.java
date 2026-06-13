package br.com.engalexandre.appestacionamento.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "convenios")
public class ConvenioEntity extends BaseEntity {

    @Column(name = "external_id", nullable = false, unique = true, length = 120)
    private String externalId;

    @Column(name = "nome", nullable = false, unique = true, length = 160)
    private String nome;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "convenio_regras", joinColumns = @JoinColumn(name = "convenio_id"))
    @OrderColumn(name = "ordem")
    @Column(name = "regra", nullable = false, length = 80)
    private List<String> regras = new ArrayList<>();

    @Column(name = "valor_meia_hora", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorMeiaHora = BigDecimal.ZERO;

    @Column(name = "valor_bloco", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorBloco = BigDecimal.ZERO;

    @Column(name = "precisa_hora", nullable = false)
    private boolean precisaHora;

    @Column(name = "cobra_ciclos", nullable = false)
    private boolean cobraCiclos = false;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private ConvenioTipo tipo = ConvenioTipo.BLOCO;

    @OneToMany(mappedBy = "convenio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ConvenioFaixaEntity> faixas = new ArrayList<>();

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getRegras() {
        return regras;
    }

    public void setRegras(List<String> regras) {
        this.regras = regras == null ? new ArrayList<>() : new ArrayList<>(regras);
    }

    public BigDecimal getValorMeiaHora() {
        return valorMeiaHora;
    }

    public void setValorMeiaHora(BigDecimal valorMeiaHora) {
        this.valorMeiaHora = valorMeiaHora;
    }

    public BigDecimal getValorBloco() {
        return valorBloco;
    }

    public void setValorBloco(BigDecimal valorBloco) {
        this.valorBloco = valorBloco;
    }

    public boolean isPrecisaHora() {
        return precisaHora;
    }

    public void setPrecisaHora(boolean precisaHora) {
        this.precisaHora = precisaHora;
    }

    public boolean isCobraCiclos() {
        return cobraCiclos;
    }

    public void setCobraCiclos(boolean cobraCiclos) {
        this.cobraCiclos = cobraCiclos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public ConvenioTipo getTipo() {
        return tipo;
    }

    public void setTipo(ConvenioTipo tipo) {
        this.tipo = tipo;
    }

    public List<ConvenioFaixaEntity> getFaixas() {
        return faixas;
    }

    public void replaceFaixas(List<ConvenioFaixaEntity> novasFaixas) {
        this.faixas.clear();
        for (ConvenioFaixaEntity faixa : novasFaixas) {
            adicionarFaixa(faixa);
        }
    }

    public void adicionarFaixa(ConvenioFaixaEntity faixa) {
        faixa.setConvenio(this);
        this.faixas.add(faixa);
    }
}