package org.acme.experience.stichingfacts.client.service.repository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
import org.acme.experience.stichingfacts.client.model.Status;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class EnhancedFactRepositoryTest {

    @Inject
    EnhancedFactRepository repository;

    @Test
    void shouldNotFindBySource() {
        Optional<EnhancedFact> fact = repository.findBySource("web");
        assertFalse(fact.isPresent());
    }

    @Test
    @Transactional
    void shouldFindBySource() {
        EnhancedFact fact = new EnhancedFact();
        fact.setSource("api");
        fact.setStatus(new Status());
        fact.setFactId("1");
        fact._id = "1";
        repository.persist(fact);
        Optional<EnhancedFact> expectedFact = repository.findBySource(fact.getSource());
        assertTrue(expectedFact.isPresent());
    }

}