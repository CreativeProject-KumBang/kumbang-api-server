package com.se.kumbangapiserver.service;


import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BoardService {

    BoardDetailDTO getBoardDetail(String boardId);

    Long createBoard(BoardDetailDTO boardDetailDTO) throws Exception;

    void deleteBoard(String boardId);

    Long updateBoard(BoardDetailDTO boardDetailDTO);

    Page<BoardListDTO> getBoardList(Map<String, String> params, Pageable pageable);

    Boolean isLike(Map<String, String> params);

    Boolean like(Map<String, String> params);

    Boolean unlike(Map<String, String> params);

    Boolean complete(String boardId, CompleteDataDTO completeDataDTO);

    Page<BoardListDTO> getMyBoardList(String userId, Pageable pageable);

}
