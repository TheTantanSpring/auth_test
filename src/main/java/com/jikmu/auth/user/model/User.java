package com.jikmu.auth.user.model;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private UUID id;
    private String username;
    private String password;
    private String nickname;
    private UserRoleEnum userRole;

    public void update(UserRoleEnum userRole) {
        this.userRole = userRole;
    }
}
