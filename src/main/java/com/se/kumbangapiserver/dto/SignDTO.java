package com.se.kumbangapiserver.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Setter
public class SignDTO {
    private String result;
    private String message;
    private String token;
}
