package global.exception;

import global.config.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiException extends RuntimeException {

    private final HttpStatus status;
    private final BaseException exception;

    public CustomApiException(BaseException exception){
        super(exception.getMessage());
        this.exception = exception;
        this.status = exception.getStatus();
    }
}
