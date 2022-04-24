package com.se.kumbangapiserver.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpDTO {

    private String email;
    private String password;
    private String name;

}
