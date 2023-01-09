package no.fintlabs.custom.elevfravar;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.utdanning.vurdering.ElevfravarResource;
import no.fint.model.resource.utdanning.vurdering.FravarsregistreringResource;
import no.fint.model.utdanning.vurdering.Fravarsregistrering;
import no.fintlabs.adapter.ResourceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Repository
public class ElevfravarRepository implements ResourceRepository<ElevfravarResource> {

    private final List<ElevfravarResource> resources = new ArrayList<>();

    private long iterationCount = 0;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 5243; i++) {
            resources.add(createElevfravar());
        }
        log.info("Generated {} elevfravar resources", getResources().size());
    }

    @Override
    public List<ElevfravarResource> getResources() {
        return resources;
    }

    @Override
    public List<ElevfravarResource> getUpdatedResources() {
        int first = 0, max = 20;

        int count = new Random().nextInt(max) + 1;
        int start = new Random().nextInt(first, getResources().size() - count);

        List<ElevfravarResource> subList = resources.subList(start, start + count);

        //if (++iterationCount % 2 == 0) {
        if (false) {
            subList.forEach(elevfravarResource -> {
                elevfravarResource
                        .getFravar()
                        .forEach(fravarsregistreringResource ->
                                fravarsregistreringResource
                                        .setKommentar(generateComment(52)));
            });
            log.info("Resend " + subList.size() + " changed resources");
        } else {
            log.info("Resend " + subList.size() + " resources");
        }

        return subList;
    }

    private ElevfravarResource createElevfravar() {
        ElevfravarResource elevfravarResource = new ElevfravarResource();
        Identifikator identifikator = new Identifikator();
        ;
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        elevfravarResource.setFravar(generateFravarsregistreringResourceList());
        elevfravarResource.setSystemId(identifikator);

        return elevfravarResource;
    }

    private List<FravarsregistreringResource> generateFravarsregistreringResourceList() {
        List<FravarsregistreringResource> fravarsregistreringList = new ArrayList<>();
        int randomRange = (int) ((Math.random() * (25)) + 0);

        for (int i = 0; i < randomRange; i++) {
            fravarsregistreringList.add(generateFravarsregistreringResource());
        }

        return fravarsregistreringList;
    }

    private FravarsregistreringResource generateFravarsregistreringResource() {
        FravarsregistreringResource fravarsregistrering = new FravarsregistreringResource();
        Periode periode = new Periode();

        periode.setStart(new Date());
        periode.setSlutt(new Date());
        periode.setBeskrivelse(generateComment(10));
        fravarsregistrering.setForesPaVitnemal(false);
        fravarsregistrering.setKommentar(generateComment(52));
        fravarsregistrering.setPeriode(periode);

        return fravarsregistrering;
    }

    private String generateComment(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}