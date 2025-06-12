package global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // user
    USER_SIGNUP(HttpStatus.CREATED, "회원가입이 성공적으로 완료되었습니다.", "S_USER_SIGNUP"),
    USER_LOGIN(HttpStatus.OK, "로그인에 성공하였습니다.", "S_USER_LOGIN"),

    ;

    private final HttpStatus statusCode;
    private final String message;
    private final String code;
}
