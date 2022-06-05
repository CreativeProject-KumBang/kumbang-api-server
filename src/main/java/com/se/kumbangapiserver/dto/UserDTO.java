package com.se.kumbangapiserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String birthDate;
    private String password;
    private String address;
    private String name;
    private List<String> role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;
}
