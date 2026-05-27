package br.com.engalexandre.appestacionamento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.engalexandre.appestacionamento.entity.MensalistaEntity;

public interface MensalistaRepository extends JpaRepository<MensalistaEntity, String> {

    @Override
    @EntityGraph(attributePaths = "pagamentos")
    List<MensalistaEntity> findAll();

    @Override
    @EntityGraph(attributePaths = "pagamentos")
    Optional<MensalistaEntity> findById(String id);

    @EntityGraph(attributePaths = "pagamentos")
    Optional<MensalistaEntity> findByExternalId(String externalId);
}