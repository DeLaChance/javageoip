package nl.lucien.domain;

import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlResult;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import nl.lucien.adapter.UserDto;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.util.UUID;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    // TODO: replace with interface instead of implementation
    private PostgresqlConnectionFactory connectionFactory;

    @Autowired
    public UserRepositoryImpl(PostgresqlConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public Flux<User> findAll() {
        return connectionFactory.create().flatMapMany(connection ->
            connection.createStatement("select id, name, keywords from public.user")
            .execute()
        )
        .flatMap(result -> result.map((row, rowMetaData) -> User.from(row)));
    }

    @Override
    public Mono<User> findUserByUserid(String userId) {
        return connectionFactory.create().flatMapMany(connection ->
            connection.createStatement("select id, name, keywords from public.user where id = $1")
            .bind("$1", userId)
            .execute()
        )
        .flatMap(result -> result.map((row, rowMetaData) -> User.from(row)))
        .next();
    }

    @Override
    public Mono<User> insert(UserDto userDto) {

        String userId = (userDto.getId() == null) ? UUID.randomUUID().toString() : userDto.getId();
        userDto.setId(userId);

        return connectionFactory.create().flatMapMany(connection ->
                connection.createStatement("insert into public.user values ($1, $2, $3) ")
                .bind("$1", userId)
                .bind("$2", userDto.getName())
                .bind("$3", userDto.keywordsAsString())
                .execute()
            )
            .doOnError(throwable -> log.error("Cannot insert '{}' due to exception: ", userId, throwable))
            .next()
            .flatMap(result -> handleInsertOrUpdate(result, userDto));
    }

    @Override
    public Mono<User> update(String userId, UserDto userDto) {
        userDto.setId(userId);

        return connectionFactory.create().flatMapMany(connection ->
                connection.createStatement("update public.user set name = $2, keywords = $3 where id = $1")
                .bind("$1", userId)
                .bind("$2", userDto.getName())
                .bind("$3", userDto.keywordsAsString())
                .execute()
            )
            .doOnError(throwable -> log.error("Cannot update '{}' due to exception: ", userId, throwable))
            .next()
            .flatMap(result -> handleInsertOrUpdate(result, userDto));
    }

    @Override
    public Mono<User> deleteByUserId(String userId) {
        return findUserByUserid(userId).flatMap(user ->
                connectionFactory.create().flatMapMany(connection ->
                    connection.createStatement("delete from public.user where id = $1")
                    .bind("$1", userId)
                    .execute()
                )
                // TODO: what if this method throws SQL-exception here?
                .then(Mono.just(user))
            )
            .doOnError(throwable -> log.error("Cannot delete '{}' due to exception: ", userId, throwable));
    }

    private Mono<User> handleInsertOrUpdate(PostgresqlResult result, UserDto userDto) {
        return result.getRowsUpdated()
            .flatMap(numberOfRowsUpdated -> {
                if (numberOfRowsUpdated == 1) {
                    return Mono.just(User.from(userDto));
                } else {
                    return Mono.error(SQLException::new);
                }
            });
    }
}
