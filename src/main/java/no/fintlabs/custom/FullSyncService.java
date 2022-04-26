package no.fintlabs.custom;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.vurdering.FravarResource;
import no.fint.model.utdanning.elev.Elevforhold;
import no.fint.model.utdanning.elev.Skoleressurs;
import no.fint.model.utdanning.kodeverk.Fravarstype;
import no.fint.model.utdanning.timeplan.Undervisningsgruppe;
import no.fint.model.utdanning.vurdering.Fravar;
import no.fintlabs.adapter.models.FullSyncPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FullSyncService {

    private final WebClient webClient;

    private boolean started;


    private List<FravarResource> fravar = new ArrayList<>();

    public FullSyncService(WebClient webClient) {
        this.webClient = webClient;
    }


    @PostConstruct
    public void init() {

        for (int i = 0; i < 5000; i++) {
            fravar.add(createFravar());
        }
        log.info("{}", fravar.size());
        //doFullSync();

//        FullSyncEntity.<FravarResource>builder()
//                .resources(Collections.emptyList())
//                .metadata(FullSyncEntity.Metadata.builder()
//                        .orgId()
//                        .build()
//                )
//                .build()
    }

    public void start() {
        log.info("Started full sync service.");
        started = true;
        doFullSync();
    }

    public void stop() {
        log.info("Stopped full sync service.");
        started = false;
    }

    @Scheduled(fixedRateString = "360000")
    private void doFullSync() {
        if (started) {
            log.info("Starting full sync...");
            FullSyncPage<FravarResource> fullSyncEntity = FullSyncPage.<FravarResource>builder()
                    .resources(fravar)
                    .metadata(FullSyncPage.Metadata.builder()
                            .orgId("fintlabs.no")
                            .corrId(UUID.randomUUID().toString())
                            .totalPages(1)
                            .totalSize(fravar.size())
                            .page(1)
                            .uriRef("/utdanning/vurdering/fravar")
                            .build()
                    )
                    .build();

            webClient.post()
                    .uri("/provider/utdanning/vurdering/fravar")
                    .body(Mono.just(fullSyncEntity), FullSyncPage.class)
                    .retrieve()
                    .toBodilessEntity()
                    .subscribe(response -> {
                        log.info(response.toString());
                    });
        } else {
            log.info("Full sync services is not started yet 🧐");
        }

    }

    private FravarResource createFravar() {
        FravarResource fravarResource = new FravarResource();
        fravarResource.setForesPaVitnemal(false);
        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        fravarResource.setSystemId(identifikator);
        fravarResource.setKommentar(generateComment(52));
        Periode periode = new Periode();
        periode.setStart(new Date());
        periode.setSlutt(new Date());
        periode.setBeskrivelse(generateComment(10));
        fravarResource.setGjelderPeriode(periode);
        fravarResource.addFravarstype(Link.with(Fravarstype.class, "systemid", generateComment(1)));
        fravarResource.addElevforhold(Link.with(Elevforhold.class, "systemid", generateComment(2)));
        fravarResource.addElevforhold(Link.with(Skoleressurs.class, "systemid", generateComment(2)));
        fravarResource.addUndervisningsgruppe(Link.with(Undervisningsgruppe.class, "systemid", generateComment(1)));
        fravarResource.addSelf(Link.with(Fravar.class, "systemid", identifikator.getIdentifikatorverdi()));

        return fravarResource;
    }

    public String generateComment(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
