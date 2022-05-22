package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import com.se.kumbangapiserver.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {

    Long makeChatRoom(Long boardId);

    Page<ChatDataDTO> getChatHistory(Long chatRoomId, Pageable pageable);

    Page<ChatRoomDTO> getChatRoomsBoardList(Pageable pageable);


    void appendChat(ChatDataDTO chatDataDTO);

    void readChat(Long messageId);
}
