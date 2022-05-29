package no.fintlabs.custom.fravar;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fintlabs.adapter.ResourcePublisher;
import no.fintlabs.adapter.ResourceRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

//@Data
@Slf4j
@Service
public class FravarPublisher extends ResourcePublisher<FravarResource, ResourceRepository<FravarResource>> {


    public FravarPublisher(FravarRepository repository) {
        super(repository);
    }


    public void publishTestData() {
        submit(repository.getResources().subList(0, 5));
    }


}
