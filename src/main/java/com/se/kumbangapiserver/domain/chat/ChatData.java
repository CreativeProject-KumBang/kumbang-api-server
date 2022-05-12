package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "chat_data")
@Entity
@RequiredArgsConstructor
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

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
    @JoinColumn(name = "receiver_user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


    @Column(name = "chat_data_created_at")
    private LocalDateTime createdAt;

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public ChatDataDTO toDTO() {
        return ChatDataDTO.builder()
                .id(id)
                .roomId(chatRoom.getId())
                .sender(sender.toDTO())
                .receiver(receiver.toDTO())
                .content(content)
                .createdAt(createdAt)
                .build();
    }

    public static ChatData fromDTO(ChatDataDTO chatDataDTO) {

        return ChatData.builder()
                .id(chatDataDTO.getId())
                .content(chatDataDTO.getContent())
                .sender(User.fromDTO(chatDataDTO.getSender()))
                .receiver(User.fromDTO(chatDataDTO.getReceiver()))
                .createdAt(chatDataDTO.getCreatedAt())
                .build();
    }
}
