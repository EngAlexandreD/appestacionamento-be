package br.com.engalexandre.appestacionamento.service.sync;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.engalexandre.appestacionamento.dto.crud.ConvenioRecebimentoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ConvenioResponse;
import br.com.engalexandre.appestacionamento.dto.crud.MensalistaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.MovimentacaoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ProdutoVendaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoResponse;
import br.com.engalexandre.appestacionamento.dto.crud.ServicoVendaResponse;
import br.com.engalexandre.appestacionamento.dto.crud.VagaResponse;
import br.com.engalexandre.appestacionamento.dto.sync.IncrementalSyncPullResponse;
import br.com.engalexandre.appestacionamento.dto.sync.SyncDeletionEntryResponse;
import br.com.engalexandre.appestacionamento.service.crud.ConvenioCrudService;
import br.com.engalexandre.appestacionamento.service.crud.ConvenioRecebimentoCrudService;
import br.com.engalexandre.appestacionamento.service.crud.MensalistaCrudService;
import br.com.engalexandre.appestacionamento.service.crud.MovimentacaoCrudService;
import br.com.engalexandre.appestacionamento.service.crud.ProdutoCrudService;
import br.com.engalexandre.appestacionamento.service.crud.ProdutoVendaCrudService;
import br.com.engalexandre.appestacionamento.service.crud.ServicoCrudService;
import br.com.engalexandre.appestacionamento.service.crud.ServicoVendaCrudService;
import br.com.engalexandre.appestacionamento.service.crud.VagaCrudService;

@Service
public class IncrementalSyncPullService {

    private static final Comparator<Instant> INSTANT_COMPARATOR = Comparator.nullsFirst(Comparator.naturalOrder());

    private final MensalistaCrudService mensalistaCrudService;
    private final MovimentacaoCrudService movimentacaoCrudService;
    private final ConvenioCrudService convenioCrudService;
    private final ConvenioRecebimentoCrudService convenioRecebimentoCrudService;
    private final VagaCrudService vagaCrudService;
    private final ServicoCrudService servicoCrudService;
    private final ServicoVendaCrudService servicoVendaCrudService;
    private final ProdutoCrudService produtoCrudService;
    private final ProdutoVendaCrudService produtoVendaCrudService;
    private final DeletionLogService deletionLogService;

    public IncrementalSyncPullService(
            MensalistaCrudService mensalistaCrudService,
            MovimentacaoCrudService movimentacaoCrudService,
            ConvenioCrudService convenioCrudService,
            ConvenioRecebimentoCrudService convenioRecebimentoCrudService,
            VagaCrudService vagaCrudService,
            ServicoCrudService servicoCrudService,
            ServicoVendaCrudService servicoVendaCrudService,
            ProdutoCrudService produtoCrudService,
            ProdutoVendaCrudService produtoVendaCrudService,
            DeletionLogService deletionLogService
    ) {
        this.mensalistaCrudService = mensalistaCrudService;
        this.movimentacaoCrudService = movimentacaoCrudService;
        this.convenioCrudService = convenioCrudService;
        this.convenioRecebimentoCrudService = convenioRecebimentoCrudService;
        this.vagaCrudService = vagaCrudService;
        this.servicoCrudService = servicoCrudService;
        this.servicoVendaCrudService = servicoVendaCrudService;
        this.produtoCrudService = produtoCrudService;
        this.produtoVendaCrudService = produtoVendaCrudService;
        this.deletionLogService = deletionLogService;
    }

    @Transactional(readOnly = true)
    public IncrementalSyncPullResponse pullChanges(Instant cursor) {
        List<MovimentacaoResponse> movimentacoes = filterAndSort(movimentacaoCrudService.listAll(), cursor, MovimentacaoResponse::updatedAt);
        List<MensalistaResponse> mensalistas = filterAndSort(mensalistaCrudService.listAll(), cursor, MensalistaResponse::updatedAt);
        List<ConvenioResponse> convenios = filterAndSort(convenioCrudService.listAll(), cursor, ConvenioResponse::updatedAt);
        List<ConvenioRecebimentoResponse> convenioRecebimentos = filterAndSort(convenioRecebimentoCrudService.listAll(), cursor, ConvenioRecebimentoResponse::updatedAt);
        List<VagaResponse> vagas = filterAndSort(vagaCrudService.listAll(), cursor, VagaResponse::updatedAt);
        List<ServicoResponse> servicos = filterAndSort(servicoCrudService.listAll(), cursor, ServicoResponse::updatedAt);
        List<ServicoVendaResponse> servicoVendas = filterAndSort(servicoVendaCrudService.listAll(), cursor, ServicoVendaResponse::updatedAt);
        List<ProdutoResponse> produtos = filterAndSort(produtoCrudService.listAll(), cursor, ProdutoResponse::updatedAt);
        List<ProdutoVendaResponse> produtoVendas = filterAndSort(produtoVendaCrudService.listAll(), cursor, ProdutoVendaResponse::updatedAt);
        List<SyncDeletionEntryResponse> deletions = deletionLogService.listDeletionsSince(cursor);

        Instant nextCursor = cursor;
        nextCursor = maxInstant(nextCursor, movimentacoes.stream().map(MovimentacaoResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, mensalistas.stream().map(MensalistaResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, convenios.stream().map(ConvenioResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, convenioRecebimentos.stream().map(ConvenioRecebimentoResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, vagas.stream().map(VagaResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, servicos.stream().map(ServicoResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, servicoVendas.stream().map(ServicoVendaResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, produtos.stream().map(ProdutoResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, produtoVendas.stream().map(ProdutoVendaResponse::updatedAt).max(INSTANT_COMPARATOR).orElse(null));
        nextCursor = maxInstant(nextCursor, deletions.stream().map(SyncDeletionEntryResponse::deletedAt).max(INSTANT_COMPARATOR).orElse(null));

        return new IncrementalSyncPullResponse(
                nextCursor,
            movimentacoes,
                mensalistas,
                convenios,
                convenioRecebimentos,
                vagas,
                servicos,
                servicoVendas,
                produtos,
                produtoVendas,
                deletions
        );
    }

    private <T> List<T> filterAndSort(List<T> items, Instant cursor, java.util.function.Function<T, Instant> instantExtractor) {
        return items.stream()
                .filter(item -> cursor == null || isAfter(instantExtractor.apply(item), cursor))
                .sorted((left, right) -> INSTANT_COMPARATOR.compare(instantExtractor.apply(left), instantExtractor.apply(right)))
                .toList();
    }

    private boolean isAfter(Instant value, Instant cursor) {
        return value != null && value.isAfter(cursor);
    }

    private Instant maxInstant(Instant current, Instant candidate) {
        if (current == null) {
            return candidate;
        }
        if (candidate == null) {
            return current;
        }
        return candidate.isAfter(current) ? candidate : current;
    }
}