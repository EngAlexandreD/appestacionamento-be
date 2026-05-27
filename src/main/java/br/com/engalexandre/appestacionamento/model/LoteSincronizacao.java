package br.com.engalexandre.appestacionamento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(
        name = "sync_batches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_sync_batch_device_hash",
                        columnNames = {"device_name", "snapshot_hash"}
                )
        }
)
public class LoteSincronizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    // Identifica qual terminal originou o snapshot salvo.
    @Column(name = "device_name", nullable = false, length = 120)
    private String deviceName;

    // Hash do payload para deduplicacao rapida por dispositivo.
    @Column(name = "snapshot_hash", nullable = false, length = 180)
    private String snapshotHash;

    // Versao do app remetente, util em auditorias de compatibilidade.
    @Column(name = "app_version", length = 32)
    private String appVersion;

    // Horario informado pelo cliente para o snapshot gerado.
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    // Horario efetivo em que o servidor gravou o snapshot.
    @Column(name = "synchronized_at", nullable = false)
    private Instant synchronizedAt;

    // Quantidade total de itens agregados para consultas administrativas rapidas.
    @Column(name = "total_records", nullable = false)
    private int totalRecords;

    // Payload bruto em JSON para permitir restauracao fiel do estado sincronizado.
    @Lob
    @Column(name = "payload_json", nullable = false, columnDefinition = "LONGTEXT")
    private String payloadJson;

    public String getId() {
        return id;
    }

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

    public Instant getSynchronizedAt() {
        return synchronizedAt;
    }

    public void setSynchronizedAt(Instant synchronizedAt) {
        this.synchronizedAt = synchronizedAt;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }
}