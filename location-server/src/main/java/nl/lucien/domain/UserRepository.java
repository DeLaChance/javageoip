package nl.lucien.domain;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, String> {

    Mono<User> findUserByUserid(String userId);
    Mono<User> deleteByUserId(String userId);
}
