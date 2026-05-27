package br.com.engalexandre.appestacionamento.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.engalexandre.appestacionamento.entity.ConvenioRecebimentoEntity;

public interface ConvenioRecebimentoRepository extends JpaRepository<ConvenioRecebimentoEntity, String> {

    Optional<ConvenioRecebimentoEntity> findByExternalId(String externalId);

    List<ConvenioRecebimentoEntity> findByConvenio_ExternalId(String convenioExternalId);
}
