package br.com.engalexandre.appestacionamento.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "pix_cobrancas")
public class PixCobrancaEntity extends BaseEntity {

    @Column(name = "referencia_origem_id", unique = true, nullable = false, length = 120)
    private String referenciaOrigemId;

    @Column(name = "origem_tipo", nullable = false, length = 40)
    private String origemTipo;

    @Column(name = "valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PixCobrancaStatus status;

    @Column(name = "pagbank_order_id", length = 80)
    private String pagbankOrderId;

    @Column(name = "qr_code_texto", length = 600)
    private String qrCodeTexto;

    @Column(name = "expira_em")
    private Instant expiraEm;

    @Column(name = "pago_em")
    private Instant pagoEm;

    public String getReferenciaOrigemId() {
        return referenciaOrigemId;
    }

    public void setReferenciaOrigemId(String referenciaOrigemId) {
        this.referenciaOrigemId = referenciaOrigemId;
    }

    public String getOrigemTipo() {
        return origemTipo;
    }

    public void setOrigemTipo(String origemTipo) {
        this.origemTipo = origemTipo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public PixCobrancaStatus getStatus() {
        return status;
    }

    public void setStatus(PixCobrancaStatus status) {
        this.status = status;
    }

    public String getPagbankOrderId() {
        return pagbankOrderId;
    }

    public void setPagbankOrderId(String pagbankOrderId) {
        this.pagbankOrderId = pagbankOrderId;
    }

    public String getQrCodeTexto() {
        return qrCodeTexto;
    }

    public void setQrCodeTexto(String qrCodeTexto) {
        this.qrCodeTexto = qrCodeTexto;
    }

    public Instant getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Instant expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Instant getPagoEm() {
        return pagoEm;
    }

    public void setPagoEm(Instant pagoEm) {
        this.pagoEm = pagoEm;
    }
}
