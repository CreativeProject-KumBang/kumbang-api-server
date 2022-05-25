package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
@Getter
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "chat_room_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_board_id")
    private RoomBoard roomBoard;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buyer_user_id")
    private User buyer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "last_message")
    private ChatData lastMessage;

    @Transient
    @Column(name = "is_buyer")
    private boolean isBuyer;

    public Long getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(boolean buyer) {
        isBuyer = buyer;
    }

    public boolean isBuyer() {
        return isBuyer;
    }

    public void setLastMessage(ChatData lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ChatRoomDTO toDTO() {
        return ChatRoomDTO.builder()
                .chatRoomId(id)
                .chatRoomName(UUID.fromString(name))
                .roomBoard(roomBoard.toListDTO())
                .buyer(buyer.toDTO())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .removedAt(removedAt)
                .lastMessage(lastMessage.toDTO())
                .isBuyer(isBuyer)
                .build();
    }
}

