package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.domain.chat.ChatData;
import com.se.kumbangapiserver.dto.ChatDataDTO;
import com.se.kumbangapiserver.dto.ChatRoomDTO;
import com.se.kumbangapiserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessage;

    @GetMapping("/api/whoami")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> whoAmI() {
        try {
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList(Common.getUserContext().toDTO())).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList("fail")).build());
        }
    }

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

    @MessageMapping("/publish")
    public void send(ChatDataDTO chatDataDTO) {
        try {
            log.info("send message : {}", chatDataDTO.toString());
            LocalDateTime now = LocalDateTime.now();
            chatDataDTO.setCreatedAt(now);
            chatService.appendChat(chatDataDTO);

            String url = "/user/" + chatDataDTO.getRoomId() + "/queue/messages";
            simpMessage.convertAndSend(url, chatDataDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/api/chat/read")
    @ResponseBody
    public void readChat(@RequestBody String messageId) {
        try {
            chatService.readChat(Long.valueOf(messageId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
