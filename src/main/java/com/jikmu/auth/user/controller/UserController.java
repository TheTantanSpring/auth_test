package com.jikmu.auth.user.controller;

import com.jikmu.auth.user.dto.SignupReqDto;
import com.jikmu.auth.user.dto.UserInfoResDto;
import com.jikmu.auth.user.service.UserService;
import global.CommonResponseDto;
import global.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<CommonResponseDto<UserInfoResDto>> signup(@RequestBody SignupReqDto requstDto) {

        UserInfoResDto responseDto = userService.signup(requstDto);
        return ResponseEntity.ok(new CommonResponseDto<>(SuccessCode.USER_SIGNUP, responseDto));
    }
}
