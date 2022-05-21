package com.se.kumbangapiserver.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class SignUpDTO {

    private String email;
    private String password;
    private String nickname;
    private String phone;
    private String name;
    private String address;

}
