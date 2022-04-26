package no.fintlabs.adapter;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.adapter.models.AdapterPing;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PingService {


    private final WebClient webClient;
    private final AdapterProperties props;

    private boolean started;

    public PingService(WebClient webClient, AdapterProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    public void start() {
        log.info("Started ping service.");
        started = true;
    }

    public void stop() {
        log.info("Stopped ping service.");
        started = false;
    }


    @Scheduled(fixedRateString = "#{@adapterProperties.getPingIntervalMs()}")
    public void ping() {

        if (started) {
            log.info("Pinging FINT...");
            AdapterPing adapterPing = AdapterPing.builder()
                    .time(System.currentTimeMillis())
                    .orgId(props.getOrgId())
                    .adapterId(props.getId())
                    .username(props.getUsername())
                    .build();

            webClient.post()
                    .uri("/provider/ping")
                    .body(Mono.just(adapterPing), AdapterPing.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(s -> {
                        if (s.equals("pong")) {
                            log.info("Successfully pinged FINT 🍾");
                        }
                    });
        } else {
            log.info("Ping service is not started yet!");
        }
    }


}
