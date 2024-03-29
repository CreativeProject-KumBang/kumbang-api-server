package com.se.kumbangapiserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChatDataDTO {

    private Long id;
    private Long roomId;
    private String content;
    private LocalDateTime createdAt;
    private UserDTO sender;
    private Boolean isRead;
    private Boolean isRemoved;
    private Boolean isComplete;
}
