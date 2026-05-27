package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.ConvenioEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConvenioRepository extends JpaRepository<ConvenioEntity, String> {

    @Override
    @EntityGraph(attributePaths = {"faixas", "regras"})
    List<ConvenioEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"faixas", "regras"})
    Optional<ConvenioEntity> findById(String id);

    @EntityGraph(attributePaths = {"faixas", "regras"})
    Optional<ConvenioEntity> findByExternalId(String externalId);

    boolean existsByNomeIgnoreCase(String nome);
}