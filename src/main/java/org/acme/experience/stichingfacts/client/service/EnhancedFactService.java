package org.acme.experience.stichingfacts.client.service;

import org.acme.experience.stichingfacts.client.service.repository.EnhancedFactRepository;
import org.acme.experience.stichingfacts.client.exception.FactNotFoundException;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class EnhancedFactService {

    @Inject
    EnhancedFactRepository repository;

    @Transactional
    public void setup(Set<EnhancedFact> enhancedFacts) {
        repository.persist(enhancedFacts);
    }

    @Transactional
    public void create(EnhancedFact enhancedFact) {
        repository.persist(enhancedFact);
    }


    public EnhancedFact findBySource(String source) throws FactNotFoundException {
        return repository.findBySource(source).orElseThrow(() -> new FactNotFoundException("No fact was found for source" + source));
    }

}
