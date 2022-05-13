package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Table(name = "chat_room")
@Entity
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class ChatRoom extends BaseTimeEntity {
    @Id
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "chat_room_name")
    private UUID name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_board_id")
    private RoomBoard roomBoard;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buyer_user_id")
    private User buyer;

    @Column(name = "last_message")
    private String lastMessage;

    public Long getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public ChatRoomDTO toDTO() {
        return ChatRoomDTO.builder()
                .chatRoomId(id)
                .chatRoomName(name)
                .roomBoard(roomBoard.toDetailDTO())
                .buyer(buyer.toDTO())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .removedAt(removedAt)
                .lastMessage(lastMessage)
                .build();
    }
}

