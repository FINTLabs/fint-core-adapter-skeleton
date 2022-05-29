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
    protected boolean started;

    protected final P publisher;


    protected ResourceSubscriber(WebClient webClient, AdapterProperties adapterProperties, P publisher) {
        this.webClient = webClient;
        this.adapterProperties = adapterProperties;
        this.publisher = publisher;
        //this.publisher = publisher;

        this.publisher.subscribe(this);
    }

    public void start() {
        log.info("Started sync service for {}.", getCapability().getEntityUri());
        started = true;
        //onFullSync();
    }

    public void stop() {
        log.info("Stopped sync service for {}.", getCapability().getEntityUri());
        started = false;
    }

    public abstract void onFullSync();
    public  void onDeltaSync(List<T> resources) {
        log.info("Delta syncing {} items to endpoint {}", resources.size(), getCapability().getEntityUri());
        getPages(resources, 500).
                forEach(this::post);
    }

    //public abstract <T extends FintLinks> void onDeltaSync(List<T> resources);

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

    public <T extends FintLinks> List<SyncPage<T>> getPages(List<T> resources, int pageSize) {

        List<SyncPage<T>> pages = new ArrayList<>();
        int size = resources.size();
        String corrId = UUID.randomUUID().toString();

        for (int i = 0; i < size; i += pageSize) {
            int end = Math.min((i + pageSize), resources.size());
            List<SyncPageEntry<T>> entries = resources.subList(i, end).stream().map(SyncPageEntry::ofSystemId).collect(Collectors.toList());
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

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.info("Subscribing to resources for endpoint {}", getCapability().getEntityUri());
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(List<T> resources) {
        onDeltaSync(resources);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        log.info("Subscriber for {} is closed", getCapability().getEntityUri());
    }
}
