package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.board.BoardState;
import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.board.RoomBoardRepository;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final RoomBoardRepository roomBoardRepository;

    @Override
    public BoardDetailDTO getBoardDetail(String boardId) {
        Optional<RoomBoard> findBoard = roomBoardRepository.findById(Long.valueOf(boardId));
        if (findBoard.isPresent()) {
            BoardDetailDTO boardDetailDTO = findBoard.get().toDTO();
        }
        return null;
    }

    @Override
    @Transactional
    public Long createBoard(BoardDetailDTO boardDetailDTO) {
        RoomBoard roomBoard = RoomBoard.fromDTO(boardDetailDTO);
        roomBoard.setCreatedAt(LocalDateTime.now());
        roomBoard.setUpdatedAt(LocalDateTime.now());
        roomBoard.changeState(BoardState.OPEN);
        RoomBoard save = roomBoardRepository.save(roomBoard);

        return save.getId();
    }
}