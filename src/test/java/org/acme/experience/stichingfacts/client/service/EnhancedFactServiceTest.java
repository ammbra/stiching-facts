package org.acme.experience.stichingfacts.client.service;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import lombok.SneakyThrows;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
import org.acme.experience.stichingfacts.client.service.repository.EnhancedFactRepository;
import org.acme.experience.stichingfacts.client.exception.FactNotFoundException;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class EnhancedFactServiceTest {
    @Inject
    EnhancedFactService service;

    @InjectMock
    EnhancedFactRepository repository;

    @Test
    void setup() {
        HashSet<EnhancedFact> enhancedFacts = new HashSet<>();
        service.setup(enhancedFacts);
        verify(repository, times(1)).persist(enhancedFacts);
    }

    @SneakyThrows
    @Test
    void findByRandomness() {
        Double random = Math.random();
        when(repository.findByRandomness(random)).thenReturn(Optional.of(new EnhancedFact()));
        service.findByRandomness(random);
        verify(repository, times(1)).findByRandomness(random);
    }

    @SneakyThrows
    @Test
    void factNotFoundByRandomness() {
        Double random = Math.random();
        assertThrows(FactNotFoundException.class, () -> service.findByRandomness(random));
        verify(repository, times(1)).findByRandomness(random);
    }
}