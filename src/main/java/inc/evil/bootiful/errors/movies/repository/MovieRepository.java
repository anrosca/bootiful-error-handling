package inc.evil.bootiful.errors.movies.repository;

import inc.evil.bootiful.errors.movies.domain.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
