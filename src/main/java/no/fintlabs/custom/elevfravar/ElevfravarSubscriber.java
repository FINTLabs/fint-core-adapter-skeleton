//package no.fintlabs.custom.elevfravar;
//
//import lombok.extern.slf4j.Slf4j;
//import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
//import no.fintlabs.adapter.config.AdapterProperties;
//import no.fintlabs.adapter.datasync.ResourceSubscriber;
//import no.fintlabs.adapter.models.AdapterCapability;
//import no.fintlabs.adapter.models.SyncPageEntry;
//import no.fintlabs.adapter.validator.ValidatorService;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Slf4j
//@Service
//public class ElevfravarSubscriber extends ResourceSubscriber<ElevfravarResource, ElevfravarPublisher> {
//
//    protected ElevfravarSubscriber(WebClient webClient, AdapterProperties props, ElevfravarPublisher publisher, ValidatorService validatorService) {
//        super(webClient, props, publisher, validatorService);
//    }
//
//    @Override
//    protected AdapterCapability getCapability() {
//
//        return adapterProperties.getCapabilities().get("elevfravar");
//    }
//
//    @Override
//    protected SyncPageEntry<ElevfravarResource> createSyncPageEntry(ElevfravarResource resource) {
//
//        String identificationValue = resource.getSystemId().getIdentifikatorverdi();
//        return SyncPageEntry.of(identificationValue, resource);
//
//        // If SystemId is provided as selflink you can use this instead:
//        // return SyncPageEntry.ofSystemId(resource);
//    }
//
//}
