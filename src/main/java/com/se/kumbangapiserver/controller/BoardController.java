package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor

public class BoardController {


    private final BoardService boardService;

    @GetMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<BoardDetailDTO> getBoardDetail(@PathVariable("id") String id) {
        BoardDetailDTO boardDetail = boardService.getBoardDetail(id);
        if (boardDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(boardDetail);
    }

    @PostMapping("/api/board/new")
    @ResponseBody
    public ResponseEntity<Long> createBoard(@RequestBody BoardDetailDTO newBoard) {
        try {
            return ResponseEntity.ok(boardService.createBoard(newBoard));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteBoard(@PathVariable("id") String id) {
        try {
            boardService.deleteBoard(id);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<Long> updateBoard(@PathVariable("id") String id, @RequestBody BoardDetailDTO newBoard) {
        try {
            return ResponseEntity.ok(boardService.updateBoard(newBoard));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}
