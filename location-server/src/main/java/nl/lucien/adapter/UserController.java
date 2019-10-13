package nl.lucien.adapter;

import nl.lucien.domain.User;
import nl.lucien.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Flux<User> getUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{userId}")
    public Mono<User> findUserById(@PathVariable("userId") String userId) {
        return userRepository.findUserByUserid(userId);
    }

    @PostMapping
    public Mono<User> createUser(@RequestBody UserDto userInput) {
        User user = User.from(userInput);
        return userRepository.save(user);
    }

    @DeleteMapping("/{userId}")
    public Mono<User> deleteUserById(@PathVariable("userId") String userId) {
        return userRepository.deleteByUserId(userId);
    }

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
