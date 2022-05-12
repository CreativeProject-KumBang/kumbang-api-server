package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.user.User;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "chat_data")
@Entity
@RequiredArgsConstructor

public class ChatData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_data_id")
    private Long id;

    @Column(name = "chat_data_content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Column(name = "chat_data_created_at")
    private LocalDateTime createdAt;

}
