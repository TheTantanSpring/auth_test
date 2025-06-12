package com.jikmu.auth.user.service;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.jikmu.auth.jwt.JwtUtil;
import com.jikmu.auth.user.dto.LoginReqDto;
import com.jikmu.auth.user.dto.LoginResDto;
import com.jikmu.auth.user.dto.SignupReqDto;
import com.jikmu.auth.user.dto.UserInfoResDto;
import com.jikmu.auth.user.model.User;
import com.jikmu.auth.user.model.UserRoleEnum;
import com.jikmu.auth.user.repository.UserRepository;
import global.CommonResponseDto;
import global.config.BaseException;
import global.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public UserInfoResDto signup(SignupReqDto requstDto) {

        String password = passwordEncoder.encode(requstDto.getPassword());

        if(exitUser(requstDto)){
            throw new CustomApiException(BaseException.DUPLICATE_USER);
        }

        User user = requstDto.toEntity(requstDto, password);
        return UserInfoResDto.of(userRepository.save(user));
    }

    public LoginResDto login(LoginReqDto requstDto) {

        User foundUser = userRepository.findByUsername(requstDto.getUsername())
                .orElseThrow(() -> new CustomApiException(BaseException.INVALID_LOGIN));

        if (!passwordEncoder.matches(requstDto.getPassword(), foundUser.getPassword())) {
            throw new CustomApiException(BaseException.INVALID_LOGIN);
        }

        String accessToken = createAccessToken(foundUser);

        return LoginResDto.of(accessToken);
    }

    public UserInfoResDto changeRole(String userName) {

        User foundUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new CustomApiException(BaseException.USER_NOT_FOUND));
        foundUser.update(UserRoleEnum.ADMIN);
        userRepository.save(foundUser);
        return UserInfoResDto.of(foundUser);
    }

    private boolean exitUser(SignupReqDto requstDto){
        // nickname이 유일하다고 가정
        return userRepository.findByNickname(requstDto.getNickname());
    }

    private String createAccessToken(User user){
        String userName = user.getUsername();
        UserRoleEnum userRole = user.getUserRole();

        return jwtUtil.createToken(userName, userRole);
    }
}
