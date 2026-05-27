package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.ProdutoVendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoVendaRepository extends JpaRepository<ProdutoVendaEntity, String> {
    Optional<ProdutoVendaEntity> findByExternalId(String externalId);
}