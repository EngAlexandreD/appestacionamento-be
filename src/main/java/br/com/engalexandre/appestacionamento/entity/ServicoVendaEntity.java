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

    @Column(name = "cliente_nome", length = 160)
    private String clienteNome;

    @Column(name = "cliente_telefone", length = 40)
    private String clienteTelefone;

    @Column(name = "placa", length = 20)
    private String placa;

    @Column(name = "modelo_veiculo", length = 120)
    private String modeloVeiculo;

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

    public String getClienteNome() {
        return clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public String getClienteTelefone() {
        return clienteTelefone;
    }

    public void setClienteTelefone(String clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModeloVeiculo() {
        return modeloVeiculo;
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo;
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