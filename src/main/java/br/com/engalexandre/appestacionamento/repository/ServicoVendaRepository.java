package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.ServicoVendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicoVendaRepository extends JpaRepository<ServicoVendaEntity, String> {
    Optional<ServicoVendaEntity> findByExternalId(String externalId);
}