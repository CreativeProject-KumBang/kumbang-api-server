package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class SignController {

    private final AuthService authservice;

    @PostMapping(value = "/api/member/signin")
    @ResponseBody
    public ResponseEntity<SignDTO> signInUser(@RequestBody SignInDTO signInDTO) {
        try {
            return ResponseEntity.ok(authservice.signIn(signInDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SignDTO("fail", e.getMessage(), null));
        }

    }

    @PostMapping(value = "/api/member/signup")
    @ResponseBody
    public ResponseEntity<SignDTO> signUpUser(@RequestBody SignUpDTO signupDTO) {
        try {
            return ResponseEntity.ok(authservice.signUp(signupDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new SignDTO("fail", "가입이 완료되지 않았습니다.", null));
        }
    }

    @PostMapping(value = "/api/member/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

}
