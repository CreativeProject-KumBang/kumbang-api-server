package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.domain.chat.ChatData;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import com.se.kumbangapiserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessage;

    @GetMapping("/api/chat/user/{roomBoardId}")
    public ResponseEntity<ResponseForm<Object>> getChat(@PathVariable String roomBoardId) {

        try {
            Long userId = Common.getUserContext().getId();
            ChatRoomDTO chat = chatService.getChatRoom(Long.valueOf(roomBoardId), userId);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(chat)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }

    }

    @GetMapping("/api/chat/rooms/{roomBoardId}")
    public ResponseEntity<ResponseForm<Object>> getChatRooms(@PathVariable String roomBoardId, Pageable pageable) {
        try {
            Page<ChatRoomDTO> chatRoomsBoard = chatService.getChatRoomsBoard(Long.valueOf(roomBoardId), pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(chatRoomsBoard)).build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @MessageMapping("/broadcast")
    public void send(ChatDataDTO chatDataDTO) {
        LocalDateTime now = LocalDateTime.now();
        chatDataDTO.setCreatedAt(now);
        chatService.appendChat(chatDataDTO);

        String url = "/user/" + chatDataDTO.getRoomId() + "/queue/messages";
        simpMessage.convertAndSend(url, chatDataDTO);
    }
}
