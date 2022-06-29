package inc.evil.bootiful.errors.common.error;

import inc.evil.bootiful.errors.common.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    //    public static final String DEFAULT_ERROR_MESSAGE = "An unexpected exception occurred while processing the request";
    private final MessageSource messageSource;

    //
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    //
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> onException(Exception e, HttpServletRequest request) {
//        log.error("Exception while handling request", e);
//        String errorMessage = e.getMessage() != null ? e.getMessage() : DEFAULT_ERROR_MESSAGE;
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.name())
//                .messages(List.of(errorMessage))
//                .path(request.getServletPath())
//                .build();
//        return ResponseEntity.internalServerError()
//                .body(errorResponse);
//    }
//
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> oEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        String message = e.getMessage();
        log.error("Exception while handling request: " + message, e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.NOT_FOUND.name())
                .messages(List.of(message))
                .path(request.getServletPath())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> onMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                                   HttpServletRequest request) {
        String message = "Parameter: '" + e.getParameterName() + "' of type " + e.getParameterType().toLowerCase(Locale.ROOT) + " is required but is missing";
        log.error("Exception while handling request: " + message, e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .messages(List.of(message))
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, /*BindException.class*/})
    public ResponseEntity<ErrorResponse> onMethodArgumentNotValidException(MethodArgumentNotValidException/*BindException*/ e, HttpServletRequest request) {
        log.error("Exception while handling request.", e);
        BindingResult bindingResult = e.getBindingResult();
        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : bindingResult.getAllErrors()) {
            String resolvedMessage = messageSource.getMessage(error, Locale.US);
            if (error instanceof FieldError fieldError) {
                errorMessages.add(String.format("Field '%s' %s but value was '%s'", fieldError.getField(), resolvedMessage,
                        fieldError.getRejectedValue()));
            } else {
                errorMessages.add(resolvedMessage);
            }
        }
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> onConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("Exception while handling request.", e);
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : constraintViolations) {
            errorMessages.add(String.format("Field '%s' %s but value was '%s'", getInvalidPropertyName(violation), violation.getMessage(),
                    violation.getInvalidValue()));
        }
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .messages(errorMessages)
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    private String getInvalidPropertyName(ConstraintViolation<?> violation) {
        return StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                .map(Path.Node::getName)
                .reduce((a, b) -> b)
                .orElse(violation.getPropertyPath().toString());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        MethodParameter parameter = e.getParameter();
        String message = "Parameter: '" + parameter.getParameterName() + "' is not valid. " + "Value '" + e.getValue()
                + "' could not be bound to type: '" + parameter.getParameterType()
                .getSimpleName()
                .toLowerCase()
                + "'";
        log.error("Exception while handling request: " + message, e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .statusCode(HttpStatus.BAD_REQUEST.name())
                .messages(List.of(message))
                .path(request.getServletPath())
                .build();
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }
}
