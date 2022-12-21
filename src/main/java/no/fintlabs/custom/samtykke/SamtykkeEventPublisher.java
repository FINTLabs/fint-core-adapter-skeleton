package no.fintlabs.custom.samtykke;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.events.EventPublisher;
import no.fintlabs.adapter.events.ResourceConverter;
import no.fintlabs.adapter.models.RequestFintEvent;
import no.fintlabs.adapter.models.ResponseFintEvent;
import no.fintlabs.adapter.models.SyncPageEntry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class SamtykkeEventPublisher extends EventPublisher<SamtykkeResource> {

    public SamtykkeEventPublisher(ResourceConverter resourceConverter, WebClient webClient, AdapterProperties adapterProperties, SamtykkeRepository repository) {
        super("samtykke", SamtykkeResource.class, resourceConverter, webClient, adapterProperties, repository);
    }

    @Override
    @Scheduled(initialDelayString = "9000", fixedDelayString = "60000")
    public void doCheckForNewEvents() {
        checkForNewEvents();
    }

    @Override
    protected void handleEvent(RequestFintEvent requestFintEvent, SamtykkeResource samtykkeResource) {
        ResponseFintEvent<SamtykkeResource> response = createResponse(requestFintEvent);

        try {
            SamtykkeResource updatedResource = repository.saveResources(samtykkeResource, requestFintEvent);
            response.setValue(createSyncPageEntry(updatedResource));
        } catch (Exception exception) {
            response.setFailed(true);
            response.setErrorMessage(exception.getMessage());
            log.error("Error in repository.saveResource", exception);
        }

        submit(response);
    }

    protected SyncPageEntry<SamtykkeResource> createSyncPageEntry(SamtykkeResource resource) {
        String identificationValue = resource.getSystemId().getIdentifikatorverdi();
        return SyncPageEntry.of(identificationValue, resource);
    }
}
