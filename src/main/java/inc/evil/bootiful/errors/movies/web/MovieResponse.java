package inc.evil.bootiful.errors.movies.web;

import inc.evil.bootiful.errors.movies.domain.Movie;
import lombok.Builder;

public record MovieResponse(Long id, String name, int publishYear) {
    @Builder
    public MovieResponse {
    }

    public static MovieResponse from(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .name(movie.getName())
                .publishYear(movie.getPublishYear())
                .build();
    }
}
