package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public SignDTO signInUser(@RequestBody SignInDTO signInDTO) {
        return authservice.signIn(signInDTO);
    }

    @PostMapping(value = "/api/member/signup")
    @ResponseBody
    public SignDTO signUpUser(@RequestBody SignUpDTO signupDTO) {
        return authservice.signUp(signupDTO);
    }

    @PostMapping(value = "/api/member/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

}
