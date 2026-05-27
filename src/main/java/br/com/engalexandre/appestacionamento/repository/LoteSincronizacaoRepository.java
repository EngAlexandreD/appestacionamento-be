package br.com.engalexandre.appestacionamento.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.engalexandre.appestacionamento.model.LoteSincronizacao;

public interface LoteSincronizacaoRepository extends JpaRepository<LoteSincronizacao, String> {

    // Localiza o lote exato para evitar persistencia duplicada do mesmo snapshot.
    Optional<LoteSincronizacao> findByDeviceNameAndSnapshotHash(String deviceName, String snapshotHash);

    // Recupera o ultimo lote de um dispositivo para consultas direcionadas.
    Optional<LoteSincronizacao> findTopByDeviceNameOrderBySynchronizedAtDesc(String deviceName);

    // Recupera o ultimo lote global salvo no servidor, sem filtro por dispositivo.
    Optional<LoteSincronizacao> findTopByOrderBySynchronizedAtDesc();

    // Busca lotes do intervalo mensal para consolidacao do relatorio.
    List<LoteSincronizacao> findBySynchronizedAtBetweenOrderBySynchronizedAtDesc(Instant start, Instant end);
}