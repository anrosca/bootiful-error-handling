package inc.evil.bootiful.errors.movies.service;

public class MovieAlreadyExistsException extends RuntimeException {
    public MovieAlreadyExistsException(String message) {
        super(message);
    }
}
