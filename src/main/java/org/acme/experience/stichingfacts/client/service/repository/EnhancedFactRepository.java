package org.acme.experience.stichingfacts.client.service.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class EnhancedFactRepository implements PanacheRepository<EnhancedFact> {

    public Optional<EnhancedFact> findByRandomness(Double randomness) {
            return find("randomness=?1",  randomness).stream().findAny();
    }

}