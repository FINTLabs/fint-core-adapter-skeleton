package no.fintlabs.custom.samtykke;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.ResourcePublisher;
import no.fintlabs.adapter.ResourceRepository;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
public class SamtykkePublisher extends ResourcePublisher<SamtykkeResource, ResourceRepository<SamtykkeResource>> {

    public SamtykkePublisher(SamtykkeRepository repository, AdapterProperties adapterProperties) {
        super(repository, adapterProperties);

        log.info("SamtykkePubliser.props: " + adapterProperties.getId());
    }

    @PostConstruct
    private void test() {
        log.info("Test");
    }

    @Override
   // @Scheduled(initialDelayString = "10000", fixedRateString = "#{@adapterProperties.getFullSyncIntervalMs('samtykke')}")
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

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilityByResource("samtykke");
    }
}
