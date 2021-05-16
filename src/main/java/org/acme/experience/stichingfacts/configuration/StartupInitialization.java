package org.acme.experience.stichingfacts.configuration;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.Startup;
import lombok.SneakyThrows;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
import org.acme.experience.stichingfacts.client.model.Fact;
import org.acme.experience.stichingfacts.client.service.EnhancedFactService;
import org.acme.experience.stichingfacts.external.FactsService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Startup
@ApplicationScoped
@UnlessBuildProfile("test")
public class StartupInitialization {
    private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitialization.class);

    @Inject
    EnhancedFactService enhancedFactService;

    @Inject
    @RestClient
    FactsService factsService;

    @ConfigProperty(name = "initial-capacity")
    Integer initialCapacity;

    @SneakyThrows
    @PostConstruct
    public void init() {
        LOGGER.debug("Initializing the db from external service");
        Set<EnhancedFact> enhancedFacts = new HashSet<>(initialCapacity);
        Set<Fact> catFacts = factsService.getByTypeAsync("cat", initialCapacity).toCompletableFuture().get();
        for (Fact fact : catFacts) {
            EnhancedFact enhancedFact = new EnhancedFact(fact);
            enhancedFact.setRandomness(Math.random());
            enhancedFacts.add(enhancedFact);
        }
        enhancedFactService.setup(enhancedFacts);
        LOGGER.debug("End initialization of the db ");

    }
}