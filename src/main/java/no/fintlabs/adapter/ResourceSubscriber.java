package no.fintlabs.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fintlabs.adapter.models.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;

import static java.util.concurrent.Flow.Subscriber;

@Slf4j
public abstract class ResourceSubscriber<T extends FintLinks, P extends ResourcePublisher<T, ResourceRepository<T>>>
        implements Subscriber<List<T>> {

    private final WebClient webClient;
    protected final AdapterProperties adapterProperties;


    protected ResourceSubscriber(WebClient webClient, AdapterProperties adapterProperties, P publisher) {
        this.webClient = webClient;
        this.adapterProperties = adapterProperties;

        publisher.subscribe(this);
    }

    public void onSync(List<T> resources) {
        log.info("Syncing {} items to endpoint {}", resources.size(), getCapability().getEntityUri());
        getPages(resources, 500).
                forEach(this::post);
    }

    protected abstract AdapterCapability getCapability();


    protected void post(SyncPage<T> page) {
        webClient.post()
                .uri("/provider" + getCapability().getEntityUri())
                .body(Mono.just(page), FullSyncPage.class)
                .retrieve()
                .toBodilessEntity()
                .subscribe(response -> {
                    log.info("Posting page ({}) returned {}.", page.getMetadata().getCorrId(), response.getStatusCode());
                });
    }

    public List<SyncPage<T>> getPages(List<T> resources, int pageSize) {

        List<SyncPage<T>> pages = new ArrayList<>();
        int size = resources.size();
        String corrId = UUID.randomUUID().toString();

        for (int i = 0; i < size; i += pageSize) {
            int end = Math.min((i + pageSize), resources.size());
            List<SyncPageEntry<T>> entries = resources.subList(i, end).stream().map(resource -> createSyncPageEntry(resource)).collect(Collectors.toList());
            pages.add(FullSyncPage.<T>builder()
                    .resources(entries)
                    .metadata(SyncPageMetadata.builder()
                            .orgId(adapterProperties.getOrgId())
                            .adapterId(adapterProperties.getId())
                            .corrId(corrId)
                            .totalPages(size / pageSize)
                            .totalSize(size)
                            .pageSize(entries.size())
                            .page((i / pageSize) + 1)
                            .uriRef(getCapability().getEntityUri())
                            .time(System.currentTimeMillis())
                            .build()
                    )
                    .build());
        }

        return pages;
    }

    protected abstract SyncPageEntry<T> createSyncPageEntry(T resource);
    // SyncPageEntry.ofSystemId(resource)

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscribing to resources for endpoint {}", getCapability().getEntityUri());
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(List<T> resources) {
        onSync(resources);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }

    @Override
    public void onComplete() {
        log.info("Subscriber for {} is closed", getCapability().getEntityUri());
    }
}
