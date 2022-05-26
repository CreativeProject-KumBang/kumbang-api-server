package com.se.kumbangapiserver.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter

public class ChatRoomDTO {

    private Long chatRoomId;
    private UUID chatRoomName;
    private BoardListDTO roomBoard;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;
    private UserDTO buyer;
    private ChatDataDTO lastMessage;
    private Boolean isBuyer;
    private Boolean isNew = false;
}
