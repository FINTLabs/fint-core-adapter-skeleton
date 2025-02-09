package no.fintlabs.custom.samtykke.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fintlabs.adapter.config.AdapterProperties;
import no.fintlabs.adapter.events.EventPublisher;
import no.fintlabs.adapter.models.event.RequestFintEvent;
import no.fintlabs.adapter.models.event.ResponseFintEvent;
import no.fintlabs.adapter.models.sync.SyncPageEntry;
import no.fintlabs.custom.samtykke.SamtykkeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class SamtykkeEventPublisher extends EventPublisher<SamtykkeResource> {

    private SamtykkeResourceValidator validator;

    public SamtykkeEventPublisher(AdapterProperties adapterProperties, SamtykkeRepository repository, WebClient webClient, ObjectMapper objectMapper, SamtykkeResourceValidator validator) {
        super("samtykke", SamtykkeResource.class, webClient, adapterProperties, repository, objectMapper);
        this.validator = validator;
    }

    @Override
    @Scheduled(initialDelayString = "9000", fixedDelayString = "60000")
    public void doCheckForNewEvents() {
        checkForNewEvents();
    }

    @Override
    protected void handleEvent(RequestFintEvent requestFintEvent, SamtykkeResource samtykkeResource) {
        ResponseFintEvent response = createResponse(requestFintEvent);

        if (resourceNotValid(samtykkeResource, response)) return;

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

    protected SyncPageEntry createSyncPageEntry(SamtykkeResource resource) {
        String identificationValue = resource.getSystemId().getIdentifikatorverdi();
        return SyncPageEntry.of(identificationValue, resource);
    }

    private boolean resourceNotValid(SamtykkeResource samtykkeResource, ResponseFintEvent response) {

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(samtykkeResource, "samtykkeResource");
        validator.validate(samtykkeResource, bindingResult);

        if (bindingResult.hasErrors()){
            response.setRejected(true);
            response.setRejectReason(bindingResult.toString());
            submit(response);
        }

        return bindingResult.hasErrors();
    }
}
