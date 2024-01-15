package no.fintlabs.custom.larling;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.larling.LarlingResource;
import no.fintlabs.adapter.datasync.ResourceRepository;
import no.fintlabs.custom.DataGenerator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Repository
public class LarlingRepository implements ResourceRepository<LarlingResource> {

    private final List<LarlingResource> resources = new ArrayList<>();

    public LarlingRepository() {
        fillCache(100);
    }

    private void fillCache(int amount) {
        for (int i = 0; i < amount; i++) {
            resources.add(createLarling());
        }
    }

    private LarlingResource createLarling() {
        LarlingResource larlingResource = new LarlingResource();
        larlingResource.setKontraktstype(DataGenerator.getRandomString(10));
        larlingResource.setLaretid(DataGenerator.getPeriode());
        larlingResource.setSystemId(DataGenerator.getIdentifikator());
        return larlingResource;
    }

    @Override
    public List<LarlingResource> getResources() {
        return resources;
    }

    @Override
    public List<LarlingResource> getUpdatedResources() {
        Random random = new Random();
        int size = resources.size();
        int count = random.nextInt(20) + 1;
        int start = size > count ? random.nextInt(size - count) : 0;

        List<LarlingResource> selectedResources = new ArrayList<>(resources.subList(start, Math.min(start + count, size)));

        for (LarlingResource resource : selectedResources) {
            resource.setKontraktstype(DataGenerator.getRandomString(20));
        }

        return selectedResources;
    }

}