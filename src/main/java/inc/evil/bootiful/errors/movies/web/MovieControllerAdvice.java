package inc.evil.bootiful.errors.movies.web;

import inc.evil.bootiful.errors.common.error.ErrorResponse;
import inc.evil.bootiful.errors.common.exception.EntityNotFoundException;
import inc.evil.bootiful.errors.movies.service.MovieAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice(assignableTypes = MovieController.class)
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MovieControllerAdvice {
    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> onMovieAlreadyExistsException(MovieAlreadyExistsException e, HttpServletRequest request) {
        String message = e.getMessage();
        log.error("Exception while handling request: " + message, e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.CONFLICT.name())
                .messages(List.of(message))
                .path(request.getServletPath())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }
}
