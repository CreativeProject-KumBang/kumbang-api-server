package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.domain.chat.ChatData;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {
    ChatRoomDTO getChatRoom(Long roomBoardId, Long userId);

    Page<ChatRoomDTO> getChatRoomsBoard(Long boardId, Pageable pageable);


    ChatData appendChat(ChatDataDTO chatDataDTO);
}
