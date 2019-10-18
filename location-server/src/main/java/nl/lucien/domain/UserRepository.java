package nl.lucien.domain;

import nl.lucien.adapter.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Flux<User> findAll();
    Mono<User> findUserByUserid(String userId);
    Mono<User> insert(UserDto userDto);
    Mono<User> update(String userId, UserDto userDto);
    Mono<User> deleteByUserId(String userId);
}
