package no.fintlabs.custom.fravarsoversikt;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.ResourceHandler;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class FravarsoversiktHandler extends ResourceHandler {
    protected FravarsoversiktHandler(WebClient webClient, AdapterProperties adapterProperties) {
        super(webClient, adapterProperties);
    }

    @Override
    public void onFullSync() {

    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilities().get("fravarsoversikt");
    }
}
