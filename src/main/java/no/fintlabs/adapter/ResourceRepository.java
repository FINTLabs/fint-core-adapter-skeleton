package no.fintlabs.adapter;

import lombok.Getter;
import no.fint.model.resource.FintLinks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public abstract class ResourceRepository<T extends FintLinks> {

    private final List<T> resources = new ArrayList<>();
    public  List<T> getResources() {
        return resources;
    }

    public abstract List<T> getUpdatedResources();
}
