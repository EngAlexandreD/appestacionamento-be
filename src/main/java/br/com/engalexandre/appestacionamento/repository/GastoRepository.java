package br.com.engalexandre.appestacionamento.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.engalexandre.appestacionamento.entity.GastoEntity;

public interface GastoRepository extends JpaRepository<GastoEntity, String> {

    Optional<GastoEntity> findByExternalId(String externalId);

    List<GastoEntity> findAllByOrderByDataHoraDesc();

    List<GastoEntity> findByUpdatedAtAfterOrderByUpdatedAtAsc(Instant after);
}
