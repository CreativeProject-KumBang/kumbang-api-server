package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "chat_room")
@Entity
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


}
