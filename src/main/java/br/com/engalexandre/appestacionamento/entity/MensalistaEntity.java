package br.com.engalexandre.appestacionamento.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "mensalistas")
public class MensalistaEntity extends BaseEntity {

    @Column(name = "external_id", nullable = false, unique = true, length = 120)
    private String externalId;

    @Column(name = "nome", nullable = false, length = 160)
    private String nome;

    @Column(name = "placa", length = 20)
    private String placa;

    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "valor_mensalidade", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorMensalidade;

    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "mensalista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("dataHora DESC")
    private final List<PagamentoMensalistaEntity> pagamentos = new ArrayList<>();

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

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public BigDecimal getValorMensalidade() {
        return valorMensalidade;
    }

    public void setValorMensalidade(BigDecimal valorMensalidade) {
        this.valorMensalidade = valorMensalidade;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public List<PagamentoMensalistaEntity> getPagamentos() {
        return pagamentos;
    }

    public void replacePagamentos(List<PagamentoMensalistaEntity> novosPagamentos) {
        this.pagamentos.clear();
        for (PagamentoMensalistaEntity pagamento : novosPagamentos) {
            adicionarPagamento(pagamento);
        }
    }

    public void adicionarPagamento(PagamentoMensalistaEntity pagamento) {
        pagamento.setMensalista(this);
        this.pagamentos.add(pagamento);
    }
}