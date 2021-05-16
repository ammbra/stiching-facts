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
        String source = "api";
        when(repository.findBySource(source)).thenReturn(Optional.of(new EnhancedFact()));
        service.findBySource(source);
        verify(repository, times(1)).findBySource(source);
    }

    @SneakyThrows
    @Test
    void factNotFoundByRandomness() {
        String source = "web";
        assertThrows(FactNotFoundException.class, () -> service.findBySource(source));
        verify(repository, times(1)).findBySource(source);
    }
}