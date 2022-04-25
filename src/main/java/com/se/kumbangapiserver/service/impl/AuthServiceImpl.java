package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.config.jwt.JwtTokenProvider;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.SignDTO;
import com.se.kumbangapiserver.dto.SignInDTO;
import com.se.kumbangapiserver.dto.SignUpDTO;
import com.se.kumbangapiserver.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignDTO signUp(SignUpDTO signUpDTO) {
        SignDTO signDTO = new SignDTO();
        if (userRepository.findByEmail(signUpDTO.getEmail()).isPresent()) {
            signDTO.setResult("fail");
            signDTO.setMessage("이미 존재하는 이메일입니다.");
            return signDTO;
        }
        String encodedPw = passwordEncoder.encode(signUpDTO.getPassword());
        User newUser = User.builder()
                .email(signUpDTO.getEmail())
                .name(signUpDTO.getName())
                .password(encodedPw)
                .roles(List.of("ROLE_USER"))
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
}
