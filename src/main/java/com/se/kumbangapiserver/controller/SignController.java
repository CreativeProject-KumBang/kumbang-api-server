package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SignController {

    private final AuthService authService;

    @PostMapping(value = "/api/auth/login")
    @ResponseBody
    public ResponseEntity<SignDTO> signInUser(@RequestBody SignInDTO signInDTO) {
        try {
            SignDTO body = authService.signIn(signInDTO);
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
            log.info("signUpUser : {}", signupDTO);
            return ResponseEntity.ok(authService.signUp(signupDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new SignDTO("fail", "가입이 완료되지 않았습니다.", null, null));
        }
    }

    @PostMapping(value = "/api/auth/sendmail")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> sendAuthMail(@RequestBody Map<String, String> params) {
        try {
            log.info("sendAuthMail : {}", params.get("email"));
            authService.authEmailSend(params.get("email"));
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList("인증메일이 발송되었습니다.")).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList("인증메일 발송에 실패하였습니다.")).build());
        }
    }

    @PostMapping(value = "/api/auth/authmail")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> authMail(@RequestBody Map<String, String> params) {
        try {
            Boolean result = authService.authEmail(params.get("email"), Integer.valueOf(params.get("authkey")));
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList(result)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList("인증메일 인증에 실패하였습니다.")).build());
        }
    }

    @GetMapping(value = "/api/auth/refreshtoken")
    @ResponseBody
    public ResponseEntity<SignDTO> refreshToken(@RequestHeader(value = "REFRESHTOKEN") String refreshToken) {
        try {
            SignDTO signDTO = authService.reissueRefreshToken(refreshToken);
            return ResponseEntity.ok(signDTO);

        } catch (Exception e) {
            return ResponseEntity.ok(new SignDTO("fail", e.getMessage(), null, null));
        }
    }
}
