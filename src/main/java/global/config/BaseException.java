package global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BaseException implements ExceptionType{
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "유효성 검증에 실패하였습니다.", "E_VALID_INPUT"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다.", "E_SERVER_ERROR" ),
    UNAUTHORIZED_REQ(HttpStatus.UNAUTHORIZED, "권한이 없습니다.", "E_UNAUTHORIZED"),
    DUPLICATE_USER(HttpStatus.CONFLICT, "중복된 회원정보가 존재합니다.", "E_DUPLICATE_USER"),
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다.", "E_DUPLICATE_USER"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다.", "E_USER_NOT_FOUND"),
    ;

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
