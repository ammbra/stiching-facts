package org.acme.experience.stichingfacts.client;

import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.quarkus.cache.CacheManager;
import io.quarkus.cache.runtime.caffeine.CaffeineCache;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@QuarkusTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class FactsEndpointITest {

    private final SimpleMeterRegistry registry = new SimpleMeterRegistry();
    @Inject
    CacheManager cacheManager;

    @Test
    void shoudlGetFactByType() {
        given()
                .when().get("/api/animal?type=cat,horse")
                .then().and()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void shoudlGetFactByTypeAsync() {
        given()
                .when().get("/api/animal-async?type=cat,horse")
                .then().and()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void shoudlGetFactByTypeAndAmountAsync() {
        given()
                .when().get("/api/animal-type-async?type=cat&amount=2")
                .then()
                .statusCode(200)
                .body("$.size()", is(2),
                      "[0].type", is("cat"),
                      "[1].type", is("cat"));
    }

    @Test
    void shouldGetFactAsync() {
        given()
                .when().get("/api/animal-async/591f98703b90f7150a19c125/1")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    void shoudlMeasureCachedFactAsync() {
        //given
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache("animal-fact-async").get();
        registry.gauge("cache.size", cache, value -> cache.getSize());

        //when
        for (int i = 0; i < 5; i++) {
            given()
                    .when().get("/api/animal-async/5887e1d85c873e0011036889/1")
                    .then().statusCode(200).body(notNullValue());
            given()
                    .when().get("/api/animal-async/591f98703b90f7150a19c125/1")
                    .then().statusCode(200).body(notNullValue());
            given()
                    .when().get("/api/animal-async/5887e1d85c873e0011036889/2")
                    .then().statusCode(200).body(notNullValue());
            given()
                    .when().get("/api/animal-async/588e746706ac2b00110e59ff/3")
                    .then().statusCode(200).body(notNullValue());
        }

        //then
        Meter gauge = registry.find("cache.size").meter();
        List<Measurement> measurements = new ArrayList<>();
        gauge.measure().iterator().forEachRemaining(measurements::add);
        assertEquals(3, measurements.get(0).getValue());
    }

    @Test
    void compareResponseTimes() {
        //given
        Timer timerSync = registry.timer("sync.request.facts.duration");

        //when
        timerSync.record(() -> {
            for (int i = 0; i < 5; i++) {
                given()
                        .when().get("/api/animal?type=cat,horse")
                        .then().and()
                        .statusCode(200)
                        .body(notNullValue());
            }
        });
        double syncTime = timerSync.totalTime(MILLISECONDS);
        //given
        Timer timerASync = registry.timer("async.request.facts.duration");

        timerASync.record(() -> {
            for (int i = 0; i < 5; i++) {
                given()
                        .when().get("/api/animal-async?type=cat,horse")
                        .then().and()
                        .statusCode(200)
                        .body(notNullValue());
            }
        });
        double asyncTime = timerASync.totalTime(MILLISECONDS);

        //then
        assertTrue(syncTime > asyncTime * 2);
    }

}