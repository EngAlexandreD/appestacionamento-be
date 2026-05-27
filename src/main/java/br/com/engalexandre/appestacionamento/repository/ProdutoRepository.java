package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, String> {
    Optional<ProdutoEntity> findByExternalId(String externalId);
}