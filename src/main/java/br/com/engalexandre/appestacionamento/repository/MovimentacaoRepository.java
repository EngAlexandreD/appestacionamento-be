package br.com.engalexandre.appestacionamento.repository;

import br.com.engalexandre.appestacionamento.entity.MovimentacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<MovimentacaoEntity, String> {

    List<MovimentacaoEntity> findAllByOrderByDataHoraDesc();

    Optional<MovimentacaoEntity> findByOperacaoOrigemId(String operacaoOrigemId);
}