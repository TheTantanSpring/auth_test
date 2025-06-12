package com.jikmu.auth.user.config;

import com.jikmu.auth.user.model.User;
import com.jikmu.auth.user.model.UserRoleEnum;
import com.jikmu.auth.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        if (userRepository.findByUsername("Admin").isEmpty()) {
            User admin = User.builder()
                    .id(UUID.randomUUID())
                    .username("Admin")
                    .password(passwordEncoder.encode("Admin"))
                    .nickname("관리자")
                    .userRole(UserRoleEnum.ADMIN)
                    .build();

            userRepository.save(admin);

            log.info("ADMIN 계정 생성 완료");
        }
    }
}
