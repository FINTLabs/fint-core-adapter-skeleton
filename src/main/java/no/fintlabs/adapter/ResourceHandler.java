package no.fintlabs.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fintlabs.adapter.models.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public abstract class ResourceHandler {

    private final WebClient webClient;
    protected final AdapterProperties adapterProperties;
    protected boolean started;


    protected ResourceHandler(WebClient webClient, AdapterProperties adapterProperties) {
        this.webClient = webClient;
        this.adapterProperties = adapterProperties;
    }

    public void start() {
        log.info("Started sync service for {}.", getCapability().getEntityUri());
        started = true;
        onFullSync();
    }

    public void stop() {
        log.info("Stopped sync service for {}.", getCapability().getEntityUri());
        started = false;
    }

    public abstract void onFullSync();

    protected abstract AdapterCapability getCapability();


    protected <T extends FintLinks> void post(SyncPage<T> page) {
        webClient.post()
                .uri("/provider" + getCapability().getEntityUri())
                .body(Mono.just(page), FullSyncPage.class)
                .retrieve()
                .toBodilessEntity()
                .subscribe(response -> {
                    log.info("Posting page ({}) returned {}.", page.getMetadata().getCorrId(), response.getStatusCode());
                });
    }

    public <T extends FintLinks> List<SyncPage<T>> getPages(List<T> resources, int pageSize) {

        List<SyncPage<T>> pages = new ArrayList<>();
        int size = resources.size();
        String corrId = UUID.randomUUID().toString();

        for (int i = 0; i < size; i += pageSize) {
            pages.add(FullSyncPage.<T>builder()
                    .resources(resources.subList(i, (i + pageSize)).stream().map(SyncPageEntry::ofSystemId).collect(Collectors.toList()))
                    .metadata(SyncPageMetadata.builder()
                            .orgId("fintlabs.no")
                            .corrId(corrId)
                            .totalPages(size / pageSize)
                            .totalSize(size)
                            .page((i / pageSize) + 1)
                            .uriRef(getCapability().getEntityUri())
                            .build()
                    )
                    .build());
        }

        return pages;
    }
}
