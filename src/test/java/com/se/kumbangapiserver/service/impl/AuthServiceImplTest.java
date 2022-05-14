package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.service.AuthService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class AuthServiceImplTest {

    @Autowired
    AuthService authService;

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


}