package inc.evil.bootiful.errors.movies.service;

import inc.evil.bootiful.errors.common.exception.EntityNotFoundException;
import inc.evil.bootiful.errors.movies.domain.Movie;
import inc.evil.bootiful.errors.movies.repository.MovieRepository;
import inc.evil.bootiful.errors.movies.web.MovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getAll() {
        return movieRepository.findAll()
                .stream()
                .map(MovieResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovieResponse getById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No movie with id:' " + id + "' was found."));
        return MovieResponse.from(movie);
    }

    @Transactional
    public MovieResponse create(Movie movieToCreate) {
        Movie createdMovie = movieRepository.save(movieToCreate);
        return MovieResponse.from(createdMovie);
    }
}
