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
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessage;

    @PostMapping("/api/chat/{boardId}")
    public ResponseEntity<ResponseForm<Object>> makeChatRoom(@PathVariable String boardId) {
        try {
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList(chatService.makeChatRoom(Long.valueOf(boardId)))).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<ResponseForm<Object>> getChatRooms(Pageable pageable) {
        try {
            Page<ChatRoomDTO> chatRoomsBoard = chatService.getChatRoomsBoardList(pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(chatRoomsBoard)).build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/chat/history/{chatRoomId}")
    public ResponseEntity<ResponseForm<Object>> getChatHistory(@PathVariable String chatRoomId, Pageable pageable) {
        try {
            Page<ChatDataDTO> chatHistory = chatService.getChatHistory(Long.valueOf(chatRoomId), pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(chatHistory)).build());

        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @MessageMapping("/api/broadcast")
    public void send(ChatDataDTO chatDataDTO) {
        LocalDateTime now = LocalDateTime.now();
        chatDataDTO.setCreatedAt(now);
        chatService.appendChat(chatDataDTO);

        String url = "/user/" + chatDataDTO.getRoomId() + "/queue/messages";
        simpMessage.convertAndSend(url, chatDataDTO);
    }

    @GetMapping("/api/chat/{messageId}")
    public void readChat(@PathVariable String messageId) {
        chatService.readChat(Long.valueOf(messageId));
    }
}
