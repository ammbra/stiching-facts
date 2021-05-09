package org.acme.experience.stichingfacts.health;

import io.agroal.api.AgroalDataSource;
import org.eclipse.microprofile.health.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.SQLException;

@Readiness
@Liveness
@ApplicationScoped
public class DatabaseConnectionHealthCheck implements HealthCheck {
    static Logger LOG = LoggerFactory.getLogger(DatabaseConnectionHealthCheck.class);

    @Inject
    AgroalDataSource defaultDataSource;

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connection health check");

        try {
            simulateDatabaseConnectionVerification();
            responseBuilder.up();
        } catch (IllegalStateException e) {
            LOG.error("Unable to reach database", e);
            responseBuilder.down();
        }

        return responseBuilder.build();
    }

    private void simulateDatabaseConnectionVerification() throws IllegalStateException {
        try {
            defaultDataSource.getConnection().getClientInfo();
        } catch (SQLException e) {
            LOG.error("Unable to reach datasource due to exception", e);
            throw new IllegalStateException("Cannot contact database");
        }
    }
}