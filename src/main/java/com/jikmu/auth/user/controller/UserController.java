package com.jikmu.auth.user.controller;

import com.jikmu.auth.user.dto.LoginReqDto;
import com.jikmu.auth.user.dto.LoginResDto;
import com.jikmu.auth.user.dto.SignupReqDto;
import com.jikmu.auth.user.dto.UserInfoResDto;
import com.jikmu.auth.user.service.UserService;
import global.CommonResponseDto;
import global.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<CommonResponseDto<LoginResDto>> login(@RequestBody LoginReqDto requstDto){

        LoginResDto responseDto = userService.login(requstDto);

        return ResponseEntity.ok(new CommonResponseDto<>(SuccessCode.USER_LOGIN, responseDto));
    }

    @PatchMapping("/admin/users/{userName}/roles")
    @Operation(summary = "관리자 권한 부여")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<UserInfoResDto>> assignRole(@PathVariable(name = "userName") String userName){

        UserInfoResDto responseDto = userService.changeRole(userName);

        return ResponseEntity.ok(new CommonResponseDto<>(SuccessCode.USER_ASSIGN_ROLE, responseDto));
    }
}
