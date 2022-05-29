package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.UserDTO;
import com.se.kumbangapiserver.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/api/mypage/like-boards")
    public ResponseEntity<ResponseForm<Object>> getLikeBoards() {

        try {
            List<BoardListDTO> wishList = userService.getWishList();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(wishList)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/mypage/{userId}")
    public ResponseEntity<ResponseForm<Object>> getUser(@PathVariable String userId) {

        try {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(userService.getUser(userId))).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @PostMapping("/api/mypage/{userId}")
    public ResponseEntity<ResponseForm<Object>> updateUser(@PathVariable String userId, UserDTO userDTO) {

        try {
            userService.updateUser(userDTO);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList("success")).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @DeleteMapping("/api/user")
    public ResponseEntity<ResponseForm<Object>> deleteUser() {
        try {
            log.info("delete user");
            userService.deleteUser();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList("success")).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/mypage/history")
    public ResponseForm<Object> getHistory(Pageable pageable) {
        try {
            return ResponseForm.builder().status(Boolean.TRUE).response(userService.getHistory(pageable)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseForm.builder().status(Boolean.FALSE).response("fail").build();
        }
    }

}