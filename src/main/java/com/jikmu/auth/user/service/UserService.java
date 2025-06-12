package com.jikmu.auth.user.service;

import com.jikmu.auth.jwt.JwtUtil;
import com.jikmu.auth.user.dto.SignupReqDto;
import com.jikmu.auth.user.dto.UserInfoResDto;
import com.jikmu.auth.user.model.User;
import com.jikmu.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserInfoResDto signup(SignupReqDto requstDto) {

        String password = passwordEncoder.encode(requstDto.getPassword());

        if(exitUser(requstDto)){
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        User user = requstDto.toEntity(requstDto, password);
        return UserInfoResDto.of(userRepository.save(user));
    }

    private boolean exitUser(SignupReqDto requstDto){
        // nickname이 유일하다고 가정
        return userRepository.findByNickname(requstDto.getNickname());
    }
}
