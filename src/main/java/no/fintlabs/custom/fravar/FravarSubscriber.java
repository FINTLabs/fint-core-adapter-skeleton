package no.fintlabs.custom.fravar;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.ResourceSubscriber;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class FravarSubscriber extends ResourceSubscriber<FravarResource, FravarPublisher> {



    protected FravarSubscriber(WebClient webClient, AdapterProperties props, FravarPublisher publisher) {
        super(webClient, props, publisher);
    }


    @Override
    protected AdapterCapability getCapability() {

        return adapterProperties.getCapabilities().get("fravar");
    }
}
