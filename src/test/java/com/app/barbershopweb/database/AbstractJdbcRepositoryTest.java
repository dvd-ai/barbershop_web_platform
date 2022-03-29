package com.app.barbershopweb.database;

import com.app.barbershopweb.barbershop.repository.JdbcBarbershopRepository;
import com.app.barbershopweb.order.crud.repository.JdbcOrderRepository;
import com.app.barbershopweb.order.reservation.repository.JdbcOrderReservationRepository;
import com.app.barbershopweb.user.repository.JdbcUsersRepository;
import com.app.barbershopweb.workspace.repository.JdbcWorkspaceRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@Testcontainers
public class AbstractJdbcRepositoryTest {

    public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:10");
    private static final String flywayTestMigrationLocation = "classpath:db/migration";
    private static final DataSource dataSource;

    static {
        postgreDBContainer.start();
        dataSource = initDataSource();
        initFlyway();
    }

    @NotNull
    private static DataSource initDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(postgreDBContainer.getJdbcUrl());
        dataSource.setUser(postgreDBContainer.getUsername());
        dataSource.setPassword(postgreDBContainer.getPassword());
        return dataSource;
    }

    private static void initFlyway() {
        FluentConfiguration fluentConfiguration = Flyway.configure()
                .dataSource(dataSource)
                .locations(flywayTestMigrationLocation);
        Flyway flyway = new Flyway(fluentConfiguration);
        flyway.migrate();
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

}
