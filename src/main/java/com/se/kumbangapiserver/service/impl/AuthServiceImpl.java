package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.config.jwt.JwtTokenProvider;
import com.se.kumbangapiserver.domain.common.MailAuthRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JavaMailSender emailSender;
    private final MailAuthRepository mailAuthRepository;

    @Override
    public SignDTO signUp(SignUpDTO signUpDTO) {
        SignDTO signDTO = new SignDTO();
        if (userRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            signDTO.setResult("fail");
            signDTO.setMessage("이미 존재하는 이메일입니다.");
            return signDTO;
        }
        if (userRepository.findByNickname(signUpDTO.getNickname()).isPresent()) {
            signDTO.setResult("fail");
            signDTO.setMessage("이미 존재하는 닉네임입니다.");
            return signDTO;
        }

        String encodedPw = passwordEncoder.encode(signUpDTO.getPassword());

        LocalDateTime now = LocalDateTime.now();
        User newUser = User.builder()
                .email(signUpDTO.getEmail())
                .name(signUpDTO.getName())
                .nickname(signUpDTO.getNickname())
                .phoneNumber(signUpDTO.getPhone())
                .password(encodedPw)
                .address(signUpDTO.getAddress())
                .roles(List.of("ROLE_USER"))
                .createdAt(now)
                .updatedAt(now)
                .build();
        userRepository.save(newUser);
        signDTO.setResult("success");
        signDTO.setMessage("회원가입 성공");
        return signDTO;
    }

    @Override
    @Transactional
    public SignDTO signIn(SignInDTO signInDTO) {
        User result = userRepository.findByEmail(signInDTO.getEmail()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));
        SignDTO signDTO = new SignDTO();
        if (result.getRemovedAt()) {
            signDTO.setResult("fail");
            signDTO.setMessage("탈퇴한 회원입니다.");
            return signDTO;
        }
        if (!passwordEncoder.matches(signInDTO.getPassword(), result.getPassword())) {
            signDTO.setResult("fail");
            signDTO.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
            return signDTO;
        }

        String refreshToken = jwtTokenProvider.createRefreshToken();
        result.setRefreshToken(refreshToken);
        userRepository.save(result);

        signDTO.setResult("success");
        signDTO.setMessage("로그인 성공");
        signDTO.setAccessToken(jwtTokenProvider.createToken(String.valueOf(result.getId()), result.getRoles()));
        signDTO.setRefreshToken(refreshToken);

        return signDTO;

    }

    @Override
    public Integer authEmailSend(String email) {
        if (email.isEmpty()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply_kumbang@kumoh.ac.kr");
        message.setTo(email);
        message.setSubject("[Kumbang] 인증메일입니다.");
        Integer code = (int) (Math.random() * 1000000);
        message.setText("인증번호는 " + code + " 입니다.");
        emailSender.send(message);

        mailAuthRepository.saveMailAndCode(email, code);

        return code;
    }

    @Override
    public Boolean authEmail(String email, Integer code) {
        return mailAuthRepository.isValidCode(email, code);
    }

    @Override
    @Transactional
    public SignDTO reissueRefreshToken(String refreshToken) {
        boolean b = jwtTokenProvider.validateToken(refreshToken);
        if (!b) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 refreshToken입니다."));
        if (!user.getRefreshToken().equals(refreshToken)) {
            return new SignDTO("fail", "토큰이 일치하지 않습니다.", null, null);
        }
        String newAccessToken = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getRoles());
        String newRefreshToken = jwtTokenProvider.createRefreshToken();
        user.setRefreshToken(newRefreshToken);

        userRepository.save(user);

        return new SignDTO("success", "토큰 재발급 성공", newAccessToken, newRefreshToken);
    }
}
