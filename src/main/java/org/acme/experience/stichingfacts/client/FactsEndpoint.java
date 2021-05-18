package org.acme.experience.stichingfacts.client;

import lombok.SneakyThrows;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
import org.acme.experience.stichingfacts.client.model.Fact;
import org.acme.experience.stichingfacts.client.service.EnhancedFactService;
import org.acme.experience.stichingfacts.external.FactsService;
import org.acme.experience.stichingfacts.client.exception.FactNotFoundException;
import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Path("/api")
@GraphQLApi
public class FactsEndpoint {
    static Logger LOG = LoggerFactory.getLogger(FactsEndpoint.class);

    @Inject
    @RestClient
    FactsService factsService;

    @Inject
    EnhancedFactService enhancedFactService;

    @GET
    @Path("/animal")
    @Produces(MediaType.APPLICATION_JSON)
    @Query("allAnimalsByType")
    public Set<Fact> getByType(@QueryParam("type") String type) {
        return factsService.getByType(type.split(","));
    }

    @SneakyThrows
    @GET
    @Path("/animal-async")
    @Produces(MediaType.APPLICATION_JSON)
    @Query("allAnimalsByTypeAsync")
    public CompletionStage<Set<Fact>> getByTypeAsync(@QueryParam("type") String type) {
        return CompletableFuture.supplyAsync(() -> factsService.getByType(type.split(",")));
    }

    @GET
    @Path("/animal-type-async")
    @Produces(MediaType.APPLICATION_JSON)
    @Query("animalByTypeAndAmount")
    public CompletionStage<Set<Fact>> getByTypeAndAmount(@QueryParam("type") String type, @QueryParam("amount") Integer amount) {
        return factsService.getByTypeAsync(type, amount);
    }

    @GET
    @Path("/animal-async/{factId}/{randomness}")
    @Produces(MediaType.APPLICATION_JSON)
    @Query("animalByFactId")
    public CompletionStage<EnhancedFact> getFactAsync(@PathParam("factId") String factId, @PathParam("randomness")@DefaultValue("0.0845") Double randomness) throws FactNotFoundException {
            return factsService.getByFactIDAsync(factId).whenComplete((enhancedFact, throwable) -> {
                if ( enhancedFact.getRandomness() == null ) {
                    enhancedFact.setRandomness(randomness);
                }
            });
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Query("enhancedFactsByAnimalAndSource")
    @Path("/animal/enhanced-fact/{type}/{source}")
    public EnhancedFact getEnhancedFact(@PathParam("type") String type, @PathParam("source")String source) {
        try {
            return enhancedFactService.findBySource(source);
        } catch (FactNotFoundException e) {
            LOG.debug("Fact not found for the given type {} and source", e);
            return new EnhancedFact();
        }
    }

}
