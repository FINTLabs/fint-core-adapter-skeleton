package no.fintlabs.adapter;

import lombok.Getter;
import no.fint.model.resource.FintLinks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public interface ResourceRepository<T extends FintLinks> {

   List<T> getResources();


    List<T> getUpdatedResources();
}
