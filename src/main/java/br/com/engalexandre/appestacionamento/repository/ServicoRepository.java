package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoRepository extends JpaRepository<ServicoEntity, String> {
    Optional<ServicoEntity> findByExternalId(String externalId);
}