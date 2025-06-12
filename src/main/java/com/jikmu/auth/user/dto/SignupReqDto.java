package com.jikmu.auth.user.dto;

import com.jikmu.auth.user.model.User;
import com.jikmu.auth.user.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
public class SignupReqDto {

    private String username;
    private String password;
    private String nickname;

    public User toEntity(SignupReqDto request, String password) {
        return User.builder()
                .id(UUID.randomUUID())
                .username(request.username)
                .password(password)
                .nickname(request.nickname)
                .userRole(UserRoleEnum.USER)
                .build();
    }

}
