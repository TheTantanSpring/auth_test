package global;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ser.Serializers;
import global.config.BaseException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponseDto<T> {

    private String code;
    private String message;
    private int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public CommonResponseDto(BaseException exception) {
        this.code = exception.getErrorCode();
        this.message = exception.getMessage();
    }

    public CommonResponseDto(SuccessCode successCode, T result) {
        this.message = successCode.getMessage();
        this.result = result;
    }
}
