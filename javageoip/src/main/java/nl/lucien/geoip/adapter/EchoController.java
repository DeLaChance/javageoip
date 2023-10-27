package nl.lucien.geoip.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("")
public class EchoController {
    
    @Autowired
    public EchoController() {
    }

    @GetMapping("/echo/{message}")
    public Mono<ResponseEntity<String>> echoMessage(@PathVariable("message") String message) {
        return Mono.just(ok(message));
    }

    private <T> ResponseEntity<T> ok(T t) {
        return ResponseEntity.ok(t);
    }
    
}
