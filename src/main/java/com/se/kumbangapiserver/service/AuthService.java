package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;

import java.util.Map;

public interface AuthService {
    SignDTO signUp(SignUpDTO signUpDTO);

    SignDTO signIn(SignInDTO signInDTO);

    Integer authEmailSend(String email);

    Boolean authEmail(String email, Integer code);

    SignDTO reissueRefreshToken(String refreshToken);
}
