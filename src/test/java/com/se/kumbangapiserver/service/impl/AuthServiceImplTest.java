package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.dto.UserDTO;
import com.se.kumbangapiserver.service.AuthService;
import com.se.kumbangapiserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    Logger log = LoggerFactory.getLogger(AuthServiceImplTest.class);

    @Test
    void sendMail() {
        Integer code = authService.authEmailSend("lichee55@gmail.com");
        Boolean result = authService.authEmail("lichee55@gmail.com", code);

        log.info("code: " + code);
        assertThat(result).isTrue();
    }

    @Test
    void sendMail2() {
        Integer code = authService.authEmailSend("youngbin1000@gmail.com");
        Boolean result = authService.authEmail("youngbin1000@gmail.com", code);

        log.info("code: " + code);
        assertThat(result).isTrue();
    }

    @Test
    @Transactional
    void reissueToken() {
        authService.signUp(SignUpDTO.builder()
                .email("lichee55@gmail.com")
                .password("1q2w3e4r")
                .build());

        SignDTO signDTO = authService.signIn(new SignInDTO("lichee55@gmail.com", "1q2w3e4r"));
        String accessToken = signDTO.getAccessToken();
        String refreshToken = signDTO.getRefreshToken();

        log.info("accessToken : {}\n refreshToken : {}", accessToken, refreshToken);

        SignDTO signDTO2 = authService.reissueRefreshToken(refreshToken);

        em.clear();
        userRepository.findById(1L).ifPresent(
                find -> {
                    log.info("user : {}", find.getRefreshToken());
                }
        );

    }

}