package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.VagaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<VagaEntity, String> {
    Optional<VagaEntity> findByNumero(Integer numero);
    Optional<VagaEntity> findByExternalId(String externalId);
}