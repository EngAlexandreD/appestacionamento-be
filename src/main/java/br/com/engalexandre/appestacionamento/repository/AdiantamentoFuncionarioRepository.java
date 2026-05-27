package br.com.engalexandre.appestacionamento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.engalexandre.appestacionamento.entity.AdiantamentoFuncionarioEntity;

@Repository
public interface AdiantamentoFuncionarioRepository extends JpaRepository<AdiantamentoFuncionarioEntity, String> {

    Optional<AdiantamentoFuncionarioEntity> findByExternalId(String externalId);

    List<AdiantamentoFuncionarioEntity> findAllByOrderByDataHoraDesc();

    void deleteByExternalId(String externalId);
}
