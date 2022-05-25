package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j

public class BoardController {


    private final BoardService boardService;

    @GetMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> getBoardDetail(@PathVariable("id") String id) {
        try {
            BoardDetailDTO boardDetail = boardService.getBoardDetail(id);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(Objects.requireNonNullElse(boardDetail, "Not Found"))).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }

    }

    @PostMapping("/api/board/new")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> createBoard(@RequestBody BoardDetailDTO newBoard) {
        try {
            log.info("createBoard : {}", newBoard);
            Long board = boardService.createBoard(newBoard);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(board)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> deleteBoard(@PathVariable("id") String id) {
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList("success")).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @PutMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> updateBoard(@PathVariable("id") String id, @RequestBody BoardDetailDTO newBoard) {
        try {
            Long updatedId = boardService.updateBoard(newBoard);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(updatedId)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/board/list")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> getBoard(
            @RequestParam Map<String, String> params,
            Pageable pageable
    ) {
        try {
            Page<BoardListDTO> boardList = boardService.getBoardList(params, pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(boardList)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/board/islike")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> isLike(@RequestParam Map<String, String> params) {
        try {
            Boolean isLike = boardService.isLike(params);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(isLike)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @PostMapping("/api/board/like")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> like(@RequestParam Map<String, String> params) {
        try {
            Boolean like = boardService.like(params);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(like)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @PostMapping("/api/board/unlike")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> unlike(@RequestParam Map<String, String> params) {
        try {
            Boolean unlike = boardService.unlike(params);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(unlike)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @PostMapping("/api/board/{id}/complete")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> complete(@PathVariable("id") String id, @RequestBody CompleteDataDTO completeDataDTO) {
        try {
            Boolean complete = boardService.complete(id, completeDataDTO);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(complete)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/mypage/post")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> getUserBoard(Pageable pageable) {
        try {
            User userContext = Common.getUserContext();
            Page<BoardListDTO> boardList = boardService.getMyBoardList(String.valueOf(userContext.getId()), pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(boardList)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }
}