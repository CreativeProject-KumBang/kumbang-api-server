package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor

public class BoardController {


    private final BoardService boardService;

    @GetMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> getBoardDetail(@PathVariable("id") String id) {
        BoardDetailDTO boardDetail = boardService.getBoardDetail(id);
        if (boardDetail == null) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("Not Found")).build());
        }
        return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(boardDetail)).build());
    }

    @PostMapping("/api/board/new")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> createBoard(@RequestBody BoardDetailDTO newBoard) {
        try {
            System.out.println("newBoard = " + newBoard.toString());
            Long board = boardService.createBoard(newBoard);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(board)).build());
        } catch (Exception e) {
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
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/board/isLike")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> isLike(@RequestParam Map<String, String> params) {
        try {
            Boolean isLike = boardService.isLike(params);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(isLike)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @GetMapping("/api/board/like")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> like(@RequestParam Map<String, String> params) {
        try {
            Boolean like = boardService.like(params);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(like)).build());
        } catch (Exception e) {
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }
}
