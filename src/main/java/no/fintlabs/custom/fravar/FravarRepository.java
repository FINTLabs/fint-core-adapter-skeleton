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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Repository
public class FravarRepository extends ResourceRepository<FravarResource> {

    @PostConstruct
    public void init() {

        for (int i = 0; i < 5243; i++) {
            getResources().add(createFravar());
        }
        log.info("Generated {} fravar resources", getResources().size());


    }

    @Override
    public List<FravarResource> getUpdatedResources() {
        int min = 0;
        int start = new Random().ints(min, getResources().size())
                .findFirst()
                .orElseThrow();
        int end = new Random().ints(start, getResources().size())
                .findFirst()
                .orElseThrow();

        return getResources().subList(start, end);
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
