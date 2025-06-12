package com.jikmu.auth.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import global.config.GlobalExceptionHandler;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(GlobalExceptionHandler.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("회원가입 성공 테스트")
    void signupSuccess() throws Exception {

        String createUserJson ="{\n"
                + "    \"username\":\"signupSuccess01\",\n"
                + "    \"password\":\"signupSuccess01\",\n"
                + "    \"nickname\":\"성공테스트유저01\"\n"
                + "}";

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(JsonPath.read(responseBody, "$.result.username").toString()).isEqualTo("signupSuccess01");
                            assertThat(JsonPath.read(responseBody, "$.result.nickname").toString()).isEqualTo("성공테스트유저01");
                            assertThat(JsonPath.read(responseBody, "$.result.userRole").toString()).isEqualTo("USER");
                        }
                );
    }

    @Test
    @Order(2)
    @DisplayName("회원가입 중복 실패 테스트")
    void signupDuplFail() throws Exception {

        String createUserJson ="{\n"
                + "    \"username\":\"signupSuccess01\",\n"
                + "    \"password\":\"signupSuccess01\",\n"
                + "    \"nickname\":\"성공테스트유저01\"\n"
                + "}";

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createUserJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("E_DUPLICATE_USER"))
                .andExpect(jsonPath("$.message").value("중복된 회원정보가 존재합니다."))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    @Order(3)
    @DisplayName("로그인 성공 테스트")
    void loginSuccess() throws Exception {

        String loginJson = "{\n"
                + "    \"username\":\"signupSuccess01\",\n"
                + "    \"password\":\"signupSuccess01\"\n"
                + "}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S_USER_LOGIN"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(Optional.ofNullable(JsonPath.read(responseBody, "$.result.token"))).isNotNull();
                            String token = JsonPath.read(responseBody, "$.result.token");
                            System.out.println("발급된 JWT 토큰: " + token);
                        }
                );
    }

    @Test
    @Order(4)
    @DisplayName("로그인 실패 테스트")
    void loginFail() throws Exception {

        String loginJson = "{\n"
                + "    \"username\":\"signupSuccess02\",\n"
                + "    \"password\":\"signupSuccess011\"\n"
                + "}";

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E_DUPLICATE_USER"))
                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @Order(5)
    @DisplayName("일반 사용자가 요청할떄 권한 오류 테스트")
    void normalUserAccessDenied() throws Exception {

        String loginJson = "{\n"
                + "    \"username\":\"signupSuccess01\",\n"
                + "    \"password\":\"signupSuccess01\"\n"
                + "}";

        AtomicReference<String> token = new AtomicReference<>();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S_USER_LOGIN"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(Optional.ofNullable(JsonPath.read(responseBody, "$.result.token"))).isNotNull();
                            token.set(JsonPath.read(responseBody, "$.result.token"));
                            System.out.println("발급된 JWT 토큰: " + token);
                        }
                );

        // 권한없는 접근
        String pathVariable = "signupSuccess01";
        mockMvc.perform(patch("/admin/users/" + pathVariable + "/roles")
                        .header("Authorization", "Bearer " + token.get().substring(7)))
                .andExpect(status().isForbidden());
    }


    @Test
    @Order(6)
    @DisplayName("존재하지 않는 사용자를 ADMIN 권한 부여할때 실패 테스트")
    void notExistUserFailTest() throws Exception {

        String loginJson = "{\n"
                + "    \"username\":\"Admin\",\n"
                + "    \"password\":\"Admin\"\n"
                + "}";

        AtomicReference<String> token = new AtomicReference<>();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S_USER_LOGIN"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(Optional.ofNullable(JsonPath.read(responseBody, "$.result.token"))).isNotNull();
                            token.set(JsonPath.read(responseBody, "$.result.token"));
                            System.out.println("발급된 JWT 토큰: " + token);
                        }
                );

        // 없는 유저
        String pathVariable = "user01";
        mockMvc.perform(patch("/admin/users/" + pathVariable + "/roles")
                        .header("Authorization",token.get()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E_USER_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @Order(7)
    @DisplayName("ADMIN 권한 부여 성공 테스트")
    void assignAdminRole() throws Exception {

        String loginJson = "{\n"
                + "    \"username\":\"Admin\",\n"
                + "    \"password\":\"Admin\"\n"
                + "}";

        AtomicReference<String> token = new AtomicReference<>();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S_USER_LOGIN"))
                .andExpect(jsonPath("$.message").value("로그인에 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(Optional.ofNullable(JsonPath.read(responseBody, "$.result.token"))).isNotNull();
                            token.set(JsonPath.read(responseBody, "$.result.token"));
                            System.out.println("발급된 JWT 토큰: " + token);
                        }
                );

        String pathVariable = "signupSuccess01";
        mockMvc.perform(patch("/admin/users/" + pathVariable + "/roles")
                        .header("Authorization", token.get()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("S_USER_ASSIGN_ROLE"))
                .andExpect(jsonPath("$.message").value("권한 변경에 성공하였습니다."))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.result").exists())
                .andDo(
                        result -> {
                            String responseBody = result.getResponse().getContentAsString();

                            assertThat(JsonPath.read(responseBody, "$.result.username").toString()).isEqualTo("signupSuccess01");
                            assertThat(JsonPath.read(responseBody, "$.result.nickname").toString()).isEqualTo("성공테스트유저01");

                            // USER -> ADMIN 변화
                            assertThat(JsonPath.read(responseBody, "$.result.userRole").toString()).isEqualTo("ADMIN");
                        }
                );
    }
}