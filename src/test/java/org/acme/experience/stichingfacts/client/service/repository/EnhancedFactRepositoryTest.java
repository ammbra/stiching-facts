package org.acme.experience.stichingfacts.client.service.repository;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
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
    void shouldNotFindByRandomness() {
        Optional<EnhancedFact> fact = repository.findByRandomness(0.097);
        assertFalse(fact.isPresent());
    }

    @Test
    @Transactional
    void shouldFindByRandomness() {
        EnhancedFact fact = new EnhancedFact();
        fact.randomness = 0.90;
        fact.set_id("1");
        repository.persist(fact);
        Optional<EnhancedFact> expectedFact = repository.findByRandomness(fact.randomness);
        assertTrue(expectedFact.isPresent());
    }

}