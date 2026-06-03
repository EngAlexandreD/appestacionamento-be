package br.com.engalexandre.appestacionamento.dto.sync;

import java.time.Instant;
import java.util.List;

import br.com.engalexandre.appestacionamento.dto.crud.AdiantamentoFuncionarioResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioResponse;
import br.com.engalexandre.appestacionamento.dto.crud.MensalistaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.VagaResponse;

public record IncrementalSyncPullResponse(
        Instant nextCursor,
        List<MovimentacaoResponse> movimentacoes,
        List<MensalistaResponse> mensalistas,
        List<ConvenioResponse> convenios,
        List<ConvenioRecebimentoResponse> convenioRecebimentos,
        List<VagaResponse> vagas,
        List<ServicoResponse> servicos,
        List<ServicoVendaResponse> servicoVendas,
        List<ProdutoResponse> produtos,
        List<ProdutoVendaResponse> produtoVendas,
        List<AdiantamentoFuncionarioResponse> adiantamentos,
        List<SyncDeletionEntryResponse> deletions
) {
}