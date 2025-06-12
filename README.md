# 서비스 소개
Spring Boot 기반 JWT 인증/인가 및 AWS 배포   

# 배포
### Swagger UI
[swagger 링크](http://43.200.173.144:8080/swagger-ui/index.html)
### AWS EC2
- [회원가입](http://43.200.173.144:8080/signup)
- [로그인](http://43.200.173.144:8080/login)
- 관리자 권한 부여(pathVariable 필요) : http://43.200.173.144:8080/admin/users/{userName}/roles

# 요구 사항
1️⃣ Spring Boot를 이용하여 JWT 인증/인가 로직과 API를 구현한다.   
2️⃣ **Junit** 기반의 테스트 코드를 작성한다.   
3️⃣ **Swagger** 로 API를 문서화 한다.    
4️⃣ 애플리케이션을 **AWS EC2**에 배포하고, 실제 환경에서 실행되도록 구성한다.  

## 기능 개발
### 1. 회원가입 POST /signup
- 회원가입 성공
![image](https://github.com/user-attachments/assets/a91d8bcf-15a4-4c00-8b8d-41ef5d251195)   
    
- 회원가입시 nickName 중복 실패 처리

</br>

### 2. 로그인 POST /login
- 로그인 성공
![image](https://github.com/user-attachments/assets/6410232f-e33c-4910-831d-db9f6b2f3de5)   

</br>

### 3. 관리자 권한 부여 PATCH /admin/users/{userName}/roles
관리자 아이디로 로그인하면 관리자 토큰을 응답받을 수 있습니다.    
**--관리자--**    
- 아이디 : Admin   
- 비밀번호 : Admin 

</br>

- 권한 부여 성공
![image](https://github.com/user-attachments/assets/b108d73b-ab8c-4163-bc91-036b3e665762)   

## 테스트 코드 작성
테스트 결과
![image](https://github.com/user-attachments/assets/ffce427c-4117-440f-b0b5-14255e64152d)

## Swagger 문서화로 API 명세
![image](https://github.com/user-attachments/assets/638854c9-75c5-407e-862a-52de18f718a7)

## AWS EC2 배포
- http://43.200.173.144:8080
---

