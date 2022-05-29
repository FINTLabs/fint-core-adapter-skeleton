package no.fintlabs.adapter;

import no.fint.model.resource.FintLinks;

import java.util.List;
import java.util.concurrent.SubmissionPublisher;

public class ResourcePublisher<T extends FintLinks, R extends ResourceRepository<T>> extends SubmissionPublisher<List<T>> {

    protected final R repository;

    public ResourcePublisher(R repository) {
        this.repository = repository;
    }
}
