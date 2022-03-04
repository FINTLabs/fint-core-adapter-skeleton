package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.model.AdapterContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class AdapterRegisterService {

    @Value("${fint.adapter.id}")
    private String adapterId;

    @Value("${fint.adapter.username}")
    private String username;

    private final WebClient webClient;
    private final PingService pingService;

    public AdapterRegisterService(WebClient webClient, PingService pingService) {
        this.webClient = webClient;
        this.pingService = pingService;
    }

    @PostConstruct
    public void init() {

        AdapterContract adapterContract = AdapterContract.builder()
                .adapterId(adapterId)
                .orgId("fintlabs.no")
                .time(System.currentTimeMillis())
                .pingIntervalInMs(300000L)
                .username(username)
                .capabilities(List.of(
                        AdapterContract.AdapterCapability.builder()
                                .clazz("person")
                                .domain("utdanning").packageName("elev")
                                .build()
                ))
                .build();
        webClient.post()
                .uri("/provider/register")
                .body(Mono.just(adapterContract), AdapterContract.class)
                .retrieve()
                .toBodilessEntity()
                .subscribe(voidResponseEntity -> {
                    log.info(String.valueOf(voidResponseEntity.getStatusCode().value()));
                    pingService.start();
                });
                //.block();

        log.info("Keep on rocking in a free world 🇺🇦!");
    }
}
