package inc.evil.bootiful.errors.movies.web;

import inc.evil.bootiful.errors.movies.domain.Movie;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record CreateMovieRequest(@NotEmpty String name, @NotNull Integer publishYear) {
    public Movie toMovie() {
        return Movie.builder()
                .name(name)
                .publishYear(publishYear)
                .build();
    }
}
