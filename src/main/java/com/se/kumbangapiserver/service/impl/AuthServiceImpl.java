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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
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
        String encodedPw = passwordEncoder.encode(signUpDTO.getPassword());

        LocalDateTime now = LocalDateTime.now();
        User newUser = User.builder()
                .email(signUpDTO.getEmail())
                .name(signUpDTO.getName())
                .nickname(signUpDTO.getNickname())
                .phoneNumber(signUpDTO.getPhoneNumber())
                .birthDate(signUpDTO.getBirthDate())
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
    public SignDTO signIn(SignInDTO signInDTO) {
        User result = (User) customUserDetailService.loadUserByUsername(signInDTO.getEmail());
        SignDTO signDTO = new SignDTO();
        if (!passwordEncoder.matches(signInDTO.getPassword(), result.getPassword())) {
            signDTO.setResult("fail");
            signDTO.setMessage("아이디 혹은 비밀번호가 일치하지 않습니다.");
            return signDTO;
        }

        signDTO.setResult("success");
        signDTO.setMessage("로그인 성공");
        signDTO.setToken(jwtTokenProvider.createToken(result.getEmail(), result.getRoles()));
        return signDTO;

    }

    @Override
    public Integer authEmailSend(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply_kumbang@gmail.com");
        message.setTo(email);
        message.setSubject("[Kumbang] 이메일 인증");
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


}
