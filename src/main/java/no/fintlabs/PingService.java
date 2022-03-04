package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.model.AdapterPing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PingService {

    @Value("${fint.adapter.id}")
    private String adapterId;

    @Value("${fint.adapter.username}")
    private String username;

    private final WebClient webClient;

    private boolean started;

    public PingService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void start() {
        log.info("Starting ping service");
        started = true;
    }

    public void stop() {
        log.info("Stopping ping service");
        started = false;
    }

    @Scheduled(fixedRate = 10000L)
    public void ping() {
        if (started) {
            log.info("Pinging FINT...");
            AdapterPing adapterPing = AdapterPing.builder()
                    .time(System.currentTimeMillis())
                    .orgId("fintlabs.no")
                    .adapterId(adapterId)
                    .username(username)
                    .build();

            webClient.post()
                    .uri("/provider/ping")
                    .body(Mono.just(adapterPing), AdapterPing.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(s -> {
                        if (s.equals("pong")) {
                            log.info("Successfully pinged FINT");
                        }
                    });
        }
        else {
            log.info("Ping service is not started yet!");
        }
    }


}
