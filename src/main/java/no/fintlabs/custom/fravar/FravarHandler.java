package no.fintlabs.custom.fravar;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.ResourceHandler;
import no.fintlabs.adapter.models.AdapterCapability;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
public class FravarHandler extends ResourceHandler {

    private final FravarService fravarService;

    protected FravarHandler(WebClient webClient, AdapterProperties props, FravarService fravarService) {
        super(webClient, props);
        this.fravarService = fravarService;
    }

    @Override
    @Scheduled(fixedRateString = "#{@adapterProperties.getFullSyncIntervalMs('fravar')}")
    public void onFullSync() {

        if (started) {
            log.info("Starting full sync...");
            getPages(fravarService.getFravar(), 500).forEach(this::post);
        } else {
            log.info("Full sync services is not started yet 🧐");
        }
    }

    @Override
    protected AdapterCapability getCapability() {
        return adapterProperties.getCapabilities().get("fravar");
    }
}
