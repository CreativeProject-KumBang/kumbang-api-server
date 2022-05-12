package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.chat.ChatData;
import com.se.kumbangapiserver.domain.chat.ChatDataRepository;
import com.se.kumbangapiserver.domain.chat.ChatRoom;
import com.se.kumbangapiserver.domain.chat.ChatRoomRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import com.se.kumbangapiserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatDataRepository chatDataRepository;

    @Override
    public ChatRoomDTO getChatRoom(Long roomBoardId, Long userId) {

        RoomBoard paramBoard = RoomBoard.builder().id(roomBoardId).build();
        User paramUser = User.builder().id(userId).build();

        LocalDateTime now = LocalDateTime.now();

        Optional<ChatRoom> findRoom =
                chatRoomRepository.findByRoomBoardAndBuyer(paramBoard, paramUser);

        if (findRoom.isEmpty()) {
            ChatRoom newChatRoom = ChatRoom.builder()
                    .name(UUID.randomUUID())
                    .roomBoard(paramBoard)
                    .buyer(paramUser)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            ChatRoom save = chatRoomRepository.save(newChatRoom);
            return save.toDTO();
        }
        return findRoom.get().toDTO();
    }

    @Override
    public Page<ChatRoomDTO> getChatRoomsBoard(Long boardId, Pageable pageable) {

        RoomBoard paramBoard = RoomBoard.builder().id(boardId).build();
        Page<ChatRoom> byRoomBoard = chatRoomRepository.findByRoomBoard(paramBoard, pageable);

        return byRoomBoard.map(ChatRoom::toDTO);
    }

    @Override
    public ChatData appendChat(ChatDataDTO chatDataDTO) {
        ChatData chatData = ChatData.fromDTO(chatDataDTO);
        ChatRoom chatRoom = chatRoomRepository.findById(chatDataDTO.getRoomId()).orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
        chatData.setChatRoom(chatRoom);
        return chatDataRepository.save(chatData);
    }


}