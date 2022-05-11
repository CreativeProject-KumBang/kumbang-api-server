package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;

    @GetMapping("/api/user/{userId}/like-boards")
    public ResponseEntity<ResponseForm<Object>> getLikeBoards(@PathVariable String userId) {

        try {
            List<BoardListDTO> wishList = userService.getWishList(userId);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(wishList)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/user/{userId}")
    public ResponseEntity<ResponseForm<Object>> getUser(@PathVariable String userId) {

        try {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(userService.getUser(userId))).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }
}
