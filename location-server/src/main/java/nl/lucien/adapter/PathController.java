package nl.lucien.adapter;

import nl.lucien.domain.Path;
import nl.lucien.domain.PathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/paths", produces = MediaType.APPLICATION_JSON_VALUE)
@EnableWebFlux
public class PathController {

    private PathRepository pathRepository;

    @Autowired
    public PathController(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    @GetMapping("/{pathId}")
    public Mono<ResponseEntity<PathResponse>> findPathById(@PathVariable("pathId") String pathId) {
        return pathRepository.findById(pathId)
            .map(PathResponse::from)
            .map(pathResponse -> new ResponseEntity<>(pathResponse, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping
    public Mono<ResponseEntity<List<PathResponse>>> queryPaths(@RequestParam("userId") String userId,
        @RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime,
        @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageNumber) {

        // TODO: actually do something with pagesize and pagenumber
        PathQuery pathQuery = PathQuery.builder()
            .userId(userId)
            .startTime(startTime)
            .endTime(endTime)
            .pageSize(pageSize)
            .pageNumber(pageNumber)
            .build();

        Mono<ResponseEntity<List<PathResponse>>> monoResponseEntity;
        if (pathQuery.isValid()) {
            monoResponseEntity = pathRepository.queryPaths(pathQuery)
                .map(PathResponse::from)
                .collectList()
                .map(pathIds -> new ResponseEntity<>(pathIds, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
                .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        } else {
            monoResponseEntity = Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }

        return monoResponseEntity;
    }

    @PostMapping("users/{userId}")
    public Mono<ResponseEntity<Path>> createPath(@PathVariable String userId) {
        return pathRepository.createPathFor(userId)
            .map(path -> new ResponseEntity<>(path, HttpStatus.OK))
            .onErrorReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
