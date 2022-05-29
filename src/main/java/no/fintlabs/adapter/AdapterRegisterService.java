package no.fintlabs.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.adapter.models.AdapterContract;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class AdapterRegisterService {


    private final WebClient webClient;
    private final PingService pingService;
    private final AdapterProperties props;

    //private final Collection<ResourceSubscriber> handlers;


    public AdapterRegisterService(WebClient webClient, PingService pingService, AdapterProperties props /*Collection<ResourceSubscriber> handlers*/) {
        this.webClient = webClient;
        this.pingService = pingService;
        this.props = props;
        //this.handlers = handlers;
    }

    @PostConstruct
    public void init() {

        AdapterContract adapterContract = AdapterContract.builder()
                .adapterId(props.getId())
                .orgId(props.getOrgId())
                .time(System.currentTimeMillis())
                .pingIntervalInMinutes(props.getPingInterval())
                .username(props.getUsername())
                .capabilities(props.adapterCapabilityToSet())
                .build();

        webClient.post()
                .uri("/provider/register")
                .body(Mono.just(adapterContract), AdapterContract.class)
                .retrieve()
                .toBodilessEntity()
                .subscribe(response -> {
                    log.info("Register return with code {}.", response.getStatusCode().value());
                    pingService.start();
                    //syncService.start();
                    //handlers.forEach(ResourceSubscriber::start);
                });

        log.info("Keep on rocking in a free world ✌️🌻️🇺🇦!");
    }
}
