package global.config;

import global.CommonResponseDto;
import global.exception.CustomApiException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomApiException.class})
    protected ResponseEntity<Object> handleCustomApiException(CustomApiException e, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(
                e,
                new CommonResponseDto<>(e.getServiceCode()),
                headers,
                e.getStatus(),
                request
        );
    }

    @ExceptionHandler(value = {ValidationException.class})
    protected ResponseEntity<Object> handleValidationException(ValidationException e, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(
                e,
                new CommonResponseDto<>(BaseException.INVALID_INPUT),
                headers,
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(value = {AuthorizationDeniedException.class})
    protected ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException e,
                                                                        WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(
                e,
                new CommonResponseDto<>(BaseException.UNAUTHORIZED_REQ),
                headers,
                HttpStatus.FORBIDDEN,
                request
        );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> internalServerError(Exception e, WebRequest request) {

        HttpHeaders headers = new HttpHeaders();
        return handleExceptionInternal(e,
                new CommonResponseDto<>(BaseException.SERVER_ERROR),
                headers,
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }

}
