package no.fintlabs;

import no.fintlabs.adapter.AdapterProperties;
import no.fintlabs.adapter.WebClientFactory;
import no.fintlabs.custom.samtykke.SamtykkePublisher;
import no.fintlabs.custom.samtykke.SamtykkeRepository;
import no.fintlabs.custom.samtykke.SamtykkeSubscriber;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitializer {

    // Initiate for org1-no:
    @Bean
    @Qualifier("samtykke-publiser-org1-no")
    SamtykkePublisher getSamtykkeOrg1(@Qualifier("org1-no") AdapterProperties props, SamtykkeRepository repo) {
        return new SamtykkePublisher(repo, props);
    }

    @Bean
    SamtykkeSubscriber getSamtykkeSubscriberOrg1(@Qualifier("org1-no") AdapterProperties props, WebClientFactory webClientFactory, @Qualifier("samtykke-publiser-org1-no") SamtykkePublisher publisher) {
        return new SamtykkeSubscriber(webClientFactory.webClient(props), props, publisher);
    }

    // Initiate for org2-no
    @Bean
    @Qualifier("samtykke-publiser-org2-no")
    SamtykkePublisher getSamtykkeOrg2(@Qualifier("org2-no") AdapterProperties props, SamtykkeRepository repo) {
        return new SamtykkePublisher(repo, props);
    }

    @Bean
    SamtykkeSubscriber getSamtykkeSubscriberOrg2(@Qualifier("org2-no") AdapterProperties props, WebClientFactory webClientFactory, @Qualifier("samtykke-publiser-org1-no") SamtykkePublisher publisher) {
        return new SamtykkeSubscriber(webClientFactory.webClient(props), props, publisher);
    }
}
