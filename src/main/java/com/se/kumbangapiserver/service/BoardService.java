package com.se.kumbangapiserver.service;


import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;

import java.util.Map;

public interface BoardService {

    BoardDetailDTO getBoardDetail(String boardId);

    Long createBoard(BoardDetailDTO boardDetailDTO) throws Exception;

    void deleteBoard(String boardId);

    Long updateBoard(BoardDetailDTO boardDetailDTO);

    BoardListDTO getBoardList(Map<String, String> params);
}
