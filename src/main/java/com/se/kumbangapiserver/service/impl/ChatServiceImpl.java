package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.domain.board.BoardState;
import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.board.RoomBoardRepository;
import com.se.kumbangapiserver.domain.chat.ChatData;
import com.se.kumbangapiserver.domain.chat.ChatDataRepository;
import com.se.kumbangapiserver.domain.chat.ChatRoom;
import com.se.kumbangapiserver.domain.chat.ChatRoomRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import com.se.kumbangapiserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatDataRepository chatDataRepository;
    private final RoomBoardRepository roomBoardRepository;


    @Override
    public Long makeChatRoom(Long boardId) {
        User contextUser = userRepository.findById(Common.getUserContext().getId()).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        RoomBoard contextBoard = roomBoardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        if (contextUser.getId().equals(contextBoard.getUser().getId())) {
            throw new RuntimeException("자신의 게시글은 채팅방에 참여할 수 없습니다.");
        }

        Optional<ChatRoom> findRoomBoard = chatRoomRepository.findByBuyerAndRoomBoard(contextUser, contextBoard);
        if (findRoomBoard.isPresent()) {
            return findRoomBoard.get().getId();
        }

        LocalDateTime now = LocalDateTime.now();
        ChatRoom newChatRoom = ChatRoom.builder()
                .name(String.valueOf(UUID.randomUUID()))
                .buyer(contextUser)
                .roomBoard(contextBoard)
                .createdAt(now)
                .updatedAt(now)
                .build();
        log.info("채팅방 생성 시도 : {}", newChatRoom);
        ChatRoom save = chatRoomRepository.save(newChatRoom);
        return save.getId();
    }

    @Override
    @Transactional
    public Page<ChatDataDTO> getChatHistory(Long chatRoomId, Pageable pageable) {
        User contextUser = userRepository.findById(Common.getUserContext().getId()).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));

        List<ChatData> chatDataList = chatDataRepository.findAllByChatRoomOrderByCreatedAtDesc(chatRoom);
        for (ChatData chatData : chatDataList) {
            if (chatData.getSender().getId().equals(contextUser.getId())) {
                continue;
            }
            chatData.setReadStatus(Boolean.TRUE);
        }

        return new PageImpl<>(chatDataList.stream().map(ChatData::toDTO).collect(Collectors.toList()), pageable, chatDataList.size());
    }


    @Override
    public Page<ChatRoomDTO> getChatRoomsBoardList(Pageable pageable) {
        User contextUser = userRepository.findById(Common.getUserContext().getId()).orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));
        Page<ChatRoom> byRoomBoard = chatRoomRepository.findDistinctByBuyerOrRoomBoard_User(contextUser, contextUser, pageable);
        Page<ChatRoom> buyerSetPage = byRoomBoard.map(e -> {
            e.setBuyer(e.getBuyer().getId().equals(contextUser.getId()));
            e.setRemoved(e.getRoomBoard().getRemovedAt() != null);
            e.setCompleted(e.getRoomBoard().getState().equals(BoardState.CLOSED));
            return e;
        });
        Page<ChatRoomDTO> map = buyerSetPage.map(ChatRoom::toDTO);
        map.forEach(e -> {
            ChatDataDTO lastMessage = e.getLastMessage();
            if ((lastMessage != null) && (!lastMessage.getSender().getId().equals(String.valueOf(contextUser.getId()))) && (!lastMessage.getIsRead())) {
                e.setIsNew(true);
            }
            
        });
        return map;
    }

    @Override
    @Transactional
    public ChatDataDTO appendChat(ChatDataDTO chatDataDTO) {
        ChatData chatData = ChatData.fromDTO(chatDataDTO);
        ChatRoom chatRoom = chatRoomRepository.findById(chatDataDTO.getRoomId()).orElseThrow(() -> new RuntimeException("채팅방이 존재하지 않습니다."));
        chatRoom.setLastMessage(chatData);
        chatRoom.setUpdatedAt(LocalDateTime.now());
        chatData.setChatRoom(chatRoom);
        chatData.setCreatedAt(LocalDateTime.now());
        chatData.setReadStatus(Boolean.FALSE);

        chatRoomRepository.save(chatRoom);
        return chatDataRepository.save(chatData).toDTO();
    }

    @Override
    @Transactional
    public void readChat(Long messageId) {
        ChatData chatData = chatDataRepository.findById(messageId).orElseThrow(() -> new RuntimeException("채팅이 존재하지 않습니다."));
        chatData.setReadStatus(Boolean.TRUE);
        chatDataRepository.save(chatData);
    }
}