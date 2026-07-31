package br.com.engalexandre.appestacionamento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.engalexandre.appestacionamento.entity.PixCobrancaEntity;

public interface PixCobrancaRepository extends JpaRepository<PixCobrancaEntity, String> {

    Optional<PixCobrancaEntity> findByReferenciaOrigemId(String referenciaOrigemId);

    Optional<PixCobrancaEntity> findByPagbankOrderId(String pagbankOrderId);
}
