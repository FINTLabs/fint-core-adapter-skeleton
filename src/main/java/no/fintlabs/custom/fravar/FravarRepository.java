package no.fintlabs.custom.fravar;

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
import no.fintlabs.adapter.ResourceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Repository
public class FravarRepository implements ResourceRepository<FravarResource> {

    private final List<FravarResource> resources = new ArrayList<>();

    private long iterationCount = 0;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 5243; i++) {
            resources.add(createFravar());
        }
        log.info("Generated {} fravar resources", getResources().size());
    }

    @Override
    public List<FravarResource> getResources() {
        return resources;
    }

    @Override
    public List<FravarResource> getUpdatedResources() {
        int first = 0, max = 20;

        int count = new Random().nextInt(max) + 1;
        int start = new Random().nextInt(first, getResources().size() - count);

        List<FravarResource> subList = resources.subList(start, start + count);

        //if (++iterationCount % 2 == 0) {
        if (false) {
            subList.forEach(fravarResource -> fravarResource.setKommentar(generateComment(52)));
            log.info("Resend " + subList.size() + " changed resources");
        } else {
            log.info("Resend " + subList.size() + " resources");
        }

        return subList;
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

    private String generateComment(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}