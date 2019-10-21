package nl.lucien.adapter;

import nl.lucien.domain.User;
import nl.lucien.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@EnableWebFlux
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Mono<ResponseEntity<List<User>>>  getUsers() {
        return userRepository.findAll()
            .collectList()
            .map(users -> new ResponseEntity<>(users, HttpStatus.OK))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/{userId}")
    public Mono<ResponseEntity<User>>  findUserById(@PathVariable("userId") String userId) {
        return userRepository.findUserByUserid(userId)
            .map(foundUser -> new ResponseEntity<>(foundUser, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Mono<ResponseEntity<User>> createUser(@RequestBody UserDto userDto) {
        return userRepository.insert(userDto)
            .map(insertedUser -> new ResponseEntity<>(insertedUser, HttpStatus.CREATED))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @PutMapping("/{userId}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable("userId") String userId, @RequestBody UserDto userDto) {
        return userRepository.update(userId, userDto)
            .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{userId}")
    public Mono<ResponseEntity<User>> deleteUserById(@PathVariable("userId") String userId) {
        return userRepository.deleteByUserId(userId)
            .map(deletedUser -> new ResponseEntity<>(deletedUser, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
