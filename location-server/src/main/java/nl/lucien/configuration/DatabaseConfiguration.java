package nl.lucien.configuration;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

import static java.lang.String.format;

@Configuration
@Slf4j
public class DatabaseConfiguration {

    @Value("${spring.data.postgres.host:localhost}")
    private String host;

    @Value("${spring.data.postgres.port:5432}")
    private Integer port;

    @Value("${spring.data.postgres.database:location-server}")
    private String database;

    @Value("${spring.data.postgres.username:test}")
    private String username;

    @Value("${spring.data.postgres.password:test}")
    private String password;

    @Bean
    public PostgresqlConnectionFactory connectionFactory() {
        log.info("Trying to set up R2DBC connection to Postgres-DB with {}:{}/{} and credentials {}:{}",
            host, port, database, username, password);

        PostgresqlConnectionConfiguration configuration = PostgresqlConnectionConfiguration.builder()
            .host(host)
            .port(port)
            .database(database)
            .username(username)
            .password(password)
            .build();

        return new PostgresqlConnectionFactory(configuration);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .username(username)
            .password(password)
            .url(format("jdbc:postgresql://%s:%s/%s", host, port, database))
            .driverClassName("org.postgresql.Driver")
            .type(PGSimpleDataSource.class)
            .build();
    }
}
