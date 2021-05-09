package org.acme.experience.stichingfacts.external;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import org.acme.experience.stichingfacts.client.model.Fact;
import org.acme.experience.stichingfacts.client.model.EnhancedFact;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Set;
import java.util.concurrent.CompletionStage;


@Path("/facts")
@RegisterRestClient
public interface FactsService {

    @GET
    @Produces("application/json")
    Set<Fact> getByType(@QueryParam("animal_type") String[] animalType);

    @GET
    @Path("/random")
    @Produces("application/json")
    CompletionStage<Set<Fact>> getByTypeAsync(@QueryParam("animal_type") String animalType, @QueryParam("amount") int amount);

    @GET
    @Path("/{factID}")
    @Produces("application/json")
    @CacheResult(cacheName = "animal-fact-async")
    CompletionStage<EnhancedFact> getByFactIDAsync(@CacheKey @PathParam("factID") String factID);
}