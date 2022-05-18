package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.config.jwt.JwtTokenProvider;
import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SignController {

    private final AuthService authservice;

    @PostMapping(value = "/api/auth/login")
    @ResponseBody
    public ResponseEntity<SignDTO> signInUser(@RequestBody SignInDTO signInDTO) {
        try {
            SignDTO body = authservice.signIn(signInDTO);
            return ResponseEntity.ok(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().body(new SignDTO("fail", e.getMessage(), null, null));
        }
    }

    @PostMapping(value = "/api/auth/signup")
    @ResponseBody
    public ResponseEntity<SignDTO> signUpUser(@RequestBody SignUpDTO signupDTO) {
        try {
            return ResponseEntity.ok(authservice.signUp(signupDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SignDTO("fail", "가입이 완료되지 않았습니다.", null, null));
        }
    }

    @GetMapping(value = "/api/member/authmail")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> sendAuthMail(String email) {
        try {
            authservice.authEmailSend(email);
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList("인증메일이 발송되었습니다.")).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList("인증메일 발송에 실패하였습니다.")).build());
        }
    }

    @PostMapping(value = "/api/member/authmail")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> authMail(@RequestBody Map<String, String> params) {
        return null;
    }

    @GetMapping(value = "/api/auth/refreshtoken")
    @ResponseBody
    public ResponseEntity<SignDTO> refreshToken(@RequestHeader(value = "REFRESHTOKEN") String refreshToken) {
        try {
            SignDTO signDTO = authservice.reissueRefreshToken(refreshToken);
            return ResponseEntity.ok(signDTO);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new SignDTO("fail", e.getMessage(), null, null));
        }
    }
}
