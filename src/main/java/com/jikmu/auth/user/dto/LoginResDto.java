package com.jikmu.auth.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
public class LoginResDto {

    private String token;

    public static LoginResDto of(String token){
        return LoginResDto.builder()
                .token(token)
                .build();
    }
}
