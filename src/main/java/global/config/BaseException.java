package global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseException implements ExceptionType{
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효성 검증에 실패하였습니다.", "E_VALID_INPUT"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다.", "E_SERVER_ERROR" ),
    UNAUTHORIZED_REQ(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", "E_UNAUTHORIZED");

    private final HttpStatus status;
    private final String message;
    private final String code;

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getErrorCode() {
        return this.code;
    }
}
