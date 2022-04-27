package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.MapAPI;
import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.board.RoomBoardRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final RoomBoardRepository roomBoardRepository;
    private final MapAPI mapAPI;

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

        List<String> coordinate = mapAPI.AddressToCoordinate(boardDetailDTO.getLocation() + " " + boardDetailDTO.getLocationDetail());

        roomBoard.setNewBoard(coordinate);

        RoomBoard save = roomBoardRepository.save(roomBoard);

        return save.getId();
    }

    @Override
    @Transactional
    public void deleteBoard(String boardId) {

        User contextUser = Common.getUserContext();

        roomBoardRepository.findById(Long.valueOf(boardId)).ifPresent(roomBoard -> {
            if (contextUser.getRoles().contains("ROLE_ADMIN")) {
                roomBoard.setRemoved();
                return;
            }
            if (!Objects.equals(roomBoard.getUser().getId(), contextUser.getId())) {
                throw new RuntimeException("권한이 없습니다.");
            }
            roomBoard.setRemoved();
        });
    }

    @Override
    @Transactional
    public Long updateBoard(BoardDetailDTO boardDetailDTO) {
        RoomBoard roomBoard = RoomBoard.fromDTO(boardDetailDTO);
        roomBoard.setUpdatedAt(LocalDateTime.now());
        RoomBoard save = roomBoardRepository.save(roomBoard);

        return save.getId();
    }


}