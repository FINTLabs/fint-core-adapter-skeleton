package no.fintlabs.custom.samtykke;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.ResourcePublisher;
import no.fintlabs.adapter.ResourceRepository;
import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.RequestFintEventCastable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class SamtykkePublisher extends ResourcePublisher<SamtykkeResource, ResourceRepository<SamtykkeResource>> {

    private final String capabilityKey = "samtykke";

    private WebClient webClient;

    public SamtykkePublisher(SamtykkeRepository repository, AdapterProperties adapterProperties, WebClient webClient) {
        super(repository, adapterProperties);
        this.webClient = webClient;
    }

    @Override
    @Scheduled(initialDelayString = "10000", fixedRateString = "#{@adapterProperties.getFullSyncIntervalMs('samtykke')}")
    public void doFullSync() {
        log.info("Start full sync for resource {}", getCapability().getEntityUri());
        submit(repository.getResources());
    }

    @Override
    @Scheduled(initialDelayString = "60000", fixedRateString = "30000")
    public void doDeltaSync() {
        log.info("Start delta sync for resource {}", getCapability().getEntityUri());
        submit(repository.getUpdatedResources());
    }

    @PostConstruct
    @Scheduled(initialDelayString = "90000", fixedDelayString = "60000")
    public void doHandleEvents() {
        log.info("Check events for resource {}", getCapability().getEntityUri());

        AdapterCapability adapterCapability = adapterProperties.getCapabilities().get(capabilityKey);
        String uri = String.format("/provider/event/%s/%s/%s/", adapterCapability.getDomainName(), adapterCapability.getPackageName(), adapterCapability.getResourceName());

        webClient.get()
                .uri(uri)
                .retrieve()
                .toEntityList(RequestFintEventString.class)
                .subscribe(response -> {
                    log.info("Event return with code {}.", response.getStatusCode().value());
                    var body = response.getBody();
                });

    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilityByResource("samtykke");
    }
}
