package no.fintlabs.custom.samtykke;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fintlabs.adapter.AdapterInstanceProperties;
import no.fintlabs.adapter.ResourceSubscriber;
import no.fintlabs.adapter.models.AdapterCapability;
import no.fintlabs.adapter.models.SyncPageEntry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class SamtykkeSubscriber extends ResourceSubscriber<SamtykkeResource, SamtykkePublisher> {

    protected SamtykkeSubscriber(WebClient webClient, @Qualifier("fint") AdapterInstanceProperties props, SamtykkePublisher publisher) {
        super(webClient, props, publisher);
    }

    @Override
    protected AdapterCapability getCapability() {

        return adapterInstanceProperties.getCapabilities().get("samtykke");
    }

    @Override
    protected SyncPageEntry<SamtykkeResource> createSyncPageEntry(SamtykkeResource resource) {

        String identificationValue = resource.getSystemId().getIdentifikatorverdi();
        return SyncPageEntry.of(identificationValue, resource);

        // If SystemId is provided as selflink you can use this instead:
        // return SyncPageEntry.ofSystemId(resource);
    }
}
