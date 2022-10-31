package no.fintlabs.custom.samtykke;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.Person;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.personvern.samtykke.Behandling;
import no.fint.model.personvern.samtykke.Samtykke;
import no.fint.model.resource.Link;
import no.fint.model.resource.personvern.samtykke.SamtykkeResource;
import no.fint.model.utdanning.elev.Elevforhold;
import no.fint.model.utdanning.elev.Skoleressurs;
import no.fint.model.utdanning.kodeverk.Fravarstype;
import no.fint.model.utdanning.timeplan.Undervisningsgruppe;
import no.fintlabs.adapter.ResourceRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Repository
public class SamtykkeRepository implements ResourceRepository<SamtykkeResource> {

    private final List<SamtykkeResource> resources = new ArrayList<>();

    private long iterationCount = 0;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 50; i++) {
            resources.add(createSamtykke());
        }
        log.info("Generated {} fravar resources", getResources().size());
    }

    @Override
    public List<SamtykkeResource> getResources() {
        return resources;
    }

    @Override
    public List<SamtykkeResource> getUpdatedResources() {
        int first = 0, max = 20;

        int count = new Random().nextInt(max) + 1;
        int start = new Random().nextInt(first, getResources().size() - count);

        List<SamtykkeResource> subList = resources.subList(start, start + count);

        //if (++iterationCount % 2 == 0) {
        if (false) {
            subList.forEach(this::setGyldighetsperiode);
            log.info("Resend " + subList.size() + " changed resources");
        } else {
            log.info("Resend " + subList.size() + " resources");
        }

        return subList;
    }

    private SamtykkeResource createSamtykke() {
        SamtykkeResource samtykkeResource = new SamtykkeResource();
        setGyldighetsperiode(samtykkeResource);

        samtykkeResource.setOpprettet(new Date());

        Identifikator identifikator = new Identifikator();
        identifikator.setIdentifikatorverdi(UUID.randomUUID().toString());
        samtykkeResource.setSystemId(identifikator);

        samtykkeResource.addBehandling(Link.with(Behandling.class, "systemid", generateComment(2)));
        samtykkeResource.addPerson(Link.with(Person.class, "systemid", generateComment(2)));
        samtykkeResource.addSelf(Link.with(Samtykke.class, "systemid", identifikator.getIdentifikatorverdi()));

        return samtykkeResource;
    }

    private void setGyldighetsperiode(SamtykkeResource resource) {
        Periode periode = new Periode();
        periode.setStart(new Date());
        periode.setSlutt(DateUtils.addMonths(new Date(), 1));
        periode.setBeskrivelse(generateComment(10));
        resource.setGyldighetsperiode(periode);
    }

    private String generateComment(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}