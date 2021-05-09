package org.acme.experience.stichingfacts.external;

import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class FactsServiceITest {

    public static final double OUTLIER_SIZE = 103.75;

    @Inject
    @RestClient
    FactsService factsService;

    @Test
    public void shouldMeasureActionsBuildingHistogram() throws Exception {
        //given
        SimpleMeterRegistry metricRegistry = new SimpleMeterRegistry();


        DistributionSummary distributionSummary = metricRegistry
                .summary("request-simple.size");
        final int amount = 5;
        int size = factsService.getByTypeAsync("cat", amount).toCompletableFuture().get().size();

        //when
        for (int i = 0; i < 100; i++) {
            distributionSummary.record(size);
        }
        distributionSummary.record(OUTLIER_SIZE);

        //then

        assertEquals(101, distributionSummary.count());
        assertEquals(100*size+OUTLIER_SIZE, distributionSummary.totalAmount());
    }

    @Test
    public void shouldMeasureActionsBuildingHistogramWithPercentiles() {
        //given
        PrometheusMeterRegistry metricRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        DistributionSummary distributionSummary = DistributionSummary.builder("request-percentiles.size")
                .publishPercentiles(0.3D, 0.5D, 0.99D).register(metricRegistry);
        String[] type = {"cat"};
        final int size = factsService.getByType(type).size();

        //when
        for (int i = 0; i < 100; i++) {
            distributionSummary.record(size+i);
        }

        //one outlier
        distributionSummary.record(OUTLIER_SIZE);

        //then
        List<Measurement> measurements = new ArrayList<>();
        Meter summary = metricRegistry.find("request-percentiles.size").meter();
        summary.measure().iterator().forEachRemaining(measurements::add);

        List<ValueAtPercentile> valueAtPercentiles = Arrays.asList(distributionSummary.takeSnapshot().percentileValues());
        List<String> result =  valueAtPercentiles
                .stream()
                .map(m -> m.percentile() + "=" + m.value()).collect(Collectors.toList());
        assertTrue(result.contains("0.99="+OUTLIER_SIZE));

    }

}