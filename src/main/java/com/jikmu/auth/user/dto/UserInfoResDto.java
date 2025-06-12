package com.jikmu.auth.user.dto;

import com.jikmu.auth.user.model.User;
import com.jikmu.auth.user.model.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoResDto {

    private String username;
    private String nickname;
    private UserRoleEnum userRole;

    public static UserInfoResDto of(User user){
        return UserInfoResDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .userRole(user.getUserRole())
                .build();
    }
}
