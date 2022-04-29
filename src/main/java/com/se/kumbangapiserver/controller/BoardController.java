package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.service.BoardService;
import com.se.kumbangapiserver.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor

public class BoardController {


    private final BoardService boardService;
    private final RegionService regionService;

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
    public ResponseEntity<ResponseForm> createBoard(@RequestBody BoardDetailDTO newBoard) {
        try {
            Long board = boardService.createBoard(newBoard);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(List.of(board.toString())).build());
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
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<Long> updateBoard(@PathVariable("id") String id, @RequestBody BoardDetailDTO newBoard) {
        try {
            return ResponseEntity.ok(boardService.updateBoard(newBoard));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/api/board/list")
    @ResponseBody
    public ResponseEntity<BoardListDTO> getBoard(@RequestParam Map<String, String> params) {

        BoardListDTO boardList = null;
        if (Integer.parseInt(params.get("scale")) < 200 && Integer.parseInt(params.get("scale")) > 0) {
            boardList = boardService.getBoardList(params);
        } else if (Integer.parseInt(params.get("scale")) > 200) {
            boardList = regionService.getRegionAvg(params);
        }

        return ResponseEntity.ok(boardList);
    }

}
