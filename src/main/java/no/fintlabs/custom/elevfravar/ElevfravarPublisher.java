//package no.fintlabs.custom.elevfravar;
//
//import lombok.extern.slf4j.Slf4j;
//import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
//import no.fintlabs.adapter.config.AdapterProperties;
//import no.fintlabs.adapter.datasync.ResourcePublisher;
//import no.fintlabs.adapter.datasync.ResourceRepository;
//import no.fintlabs.adapter.datasync.SyncData;
//import no.fintlabs.adapter.models.AdapterCapability;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class ElevfravarPublisher extends ResourcePublisher<ElevfravarResource, ResourceRepository<ElevfravarResource>> {
//
//    public ElevfravarPublisher(ElevfravarRepository repository, AdapterProperties adapterProperties) {
//        super(repository, adapterProperties);
//    }
//
//    @Override
//    @Scheduled(initialDelayString = "10000", fixedRateString = "#{@adapterProperties.getFullSyncIntervalMs('elevfravar')}")
//    public void doFullSync() {
//        log.info("Start full sync for resource {}", getCapability().getEntityUri());
//        submit(SyncData.ofPostData(repository.getResources()));
//    }
//
//    @Override
//    @Scheduled(initialDelayString = "120000", fixedRateString = "180000")
//    public void doDeltaSync() {
//        log.info("Start delta sync for resource {}", getCapability().getEntityUri());
//        submit(SyncData.ofPatchData(repository.getUpdatedResources()));
//    }
//
//    @Override
//    protected AdapterCapability getCapability() {
//        return adapterProperties.getCapabilityByResource("elevfravar");
//    }
//}
