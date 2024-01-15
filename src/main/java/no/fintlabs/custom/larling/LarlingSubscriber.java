package no.fintlabs.custom.larling;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.config.AdapterProperties;
import no.fintlabs.adapter.datasync.ResourceSubscriber;
import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.SyncPageEntry;
import no.fintlabs.adapter.validator.ValidatorService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class LarlingSubscriber extends ResourceSubscriber<LarlingResource, LarlingPublisher> {

    protected LarlingSubscriber(WebClient webClient, AdapterProperties props, LarlingPublisher publisher, ValidatorService validatorService) {
        super(webClient, props, publisher, validatorService);
    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilities().get("larling");
    }

    @Override
    protected SyncPageEntry<LarlingResource> createSyncPageEntry(LarlingResource resource) {

        String identificationValue = resource.getSystemId().getIdentifikatorverdi();
        return SyncPageEntry.of(identificationValue, resource);
    }

}
