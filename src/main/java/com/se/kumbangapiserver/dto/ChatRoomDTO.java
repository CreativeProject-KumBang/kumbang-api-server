package com.se.kumbangapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoomDTO {

    private Long chatRoomId;
    private UUID chatRoomName;
    private BoardListDTO roomBoard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;
    private UserDTO buyer;
    private String lastMessage;
    private Boolean isBuyer;
}
