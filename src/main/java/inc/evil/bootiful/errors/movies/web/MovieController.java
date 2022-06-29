package inc.evil.bootiful.errors.movies.web;

import inc.evil.bootiful.errors.movies.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@Validated
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

   /* @GetMapping
    public List<MovieResponse> getAll() {
        return movieService.getAll();
    }*/

    @GetMapping
    public MovieResponse getById(@RequestParam("id") @Min(1) Long id) {
        return movieService.getById(id);
    }

    @PostMapping
    public ResponseEntity<MovieResponse> create(@RequestBody @Validated CreateMovieRequest request) {
        MovieResponse createdMovie = movieService.create(request.toMovie());
        URI location =  MvcUriComponentsBuilder.fromMethodCall(MvcUriComponentsBuilder.on(getClass())
                        .getById(createdMovie.id()))
                .build()
                .toUri();
        return ResponseEntity.created(location)
                .body(createdMovie);
    }
}
