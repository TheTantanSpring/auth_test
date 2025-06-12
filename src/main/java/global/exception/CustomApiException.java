package global.exception;

import global.config.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomApiException extends RuntimeException {

    private final HttpStatus status;
    private final BaseException serviceCode;

    public CustomApiException(BaseException serviceCode){
        super(serviceCode.getMessage());
        this.serviceCode = serviceCode;
        this.status = serviceCode.getStatus();
    }
}
