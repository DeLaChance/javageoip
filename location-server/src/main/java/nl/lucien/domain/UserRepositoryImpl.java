package nl.lucien.domain;

import lombok.extern.slf4j.Slf4j;
import nl.lucien.adapter.UserDto;
import nl.lucien.configuration.RdbcAdapter;
import nl.lucien.configuration.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private RdbcAdapter rdbcAdapter;

    @Autowired
    public UserRepositoryImpl(RdbcAdapter rdbcAdapter) {
        this.rdbcAdapter = rdbcAdapter;
    }

    @Override
    public Flux<User> findAll() {
        String sqlQuery = "select id, name, keywords from public.user";
        return rdbcAdapter.findAll(SQLQuery.from(sqlQuery, new String[]{}), User.class);
    }

    @Override
    public Mono<User> findUserByUserid(String userId) {
        String query = "select id, name, keywords from public.user where id = $1";
        String[] args = new String[] { userId };

        return rdbcAdapter.find(SQLQuery.from(query, args), User.class);
    }

    @Override
    public Mono<User> insert(UserDto userDto) {

        String userId = (userDto.getId() == null) ? UUID.randomUUID().toString() : userDto.getId();
        userDto.setId(userId);

        String query = "insert into public.user values ($1, $2, $3)";
        String[] args = new String[] { userId, userDto.getName(), userDto.keywordsAsString() };

        return rdbcAdapter.insert(SQLQuery.from(query, args), User.class)
            .doOnError(throwable -> log.error("Cannot insert '{}' due to exception: ", userId, throwable));
    }

    @Override
    public Mono<User> update(String userId, UserDto userDto) {
        userDto.setId(userId);

        String query = "update public.user set name = $2, keywords = $3 where id = $1";
        String[] args = new String[] { userId, userDto.getName(), userDto.keywordsAsString() };

        return rdbcAdapter.update(SQLQuery.from(query, args), User.class)
            .doOnError(throwable -> log.error("Cannot update '{}' due to exception: ", userId, throwable));
    }

    @Override
    public Mono<User> deleteByUserId(String userId) {
        String query = "delete from public.user where id = $1";
        String[] args = new String[] { userId };

        return rdbcAdapter.delete(SQLQuery.from(query, args), User.class)
            .doOnError(throwable -> log.error("Cannot delete '{}' due to exception: ", userId, throwable));
    }

}
