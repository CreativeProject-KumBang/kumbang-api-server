package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.MapAPI;
import com.se.kumbangapiserver.domain.archive.RegionRepository;
import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.board.RoomBoardRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final RoomBoardRepository roomBoardRepository;
    private final RegionRepository regionRepository;
    private final MapAPI mapAPI;

    @Override
    public BoardDetailDTO getBoardDetail(String boardId) {
        Optional<RoomBoard> findBoard = roomBoardRepository.findById(Long.valueOf(boardId));
        BoardDetailDTO boardDetailDTO = null;
        if (findBoard.isPresent()) {
            boardDetailDTO = findBoard.get().toDetailDTO();
        }
        return boardDetailDTO;
    }

    @Override
    @Transactional
    public Long createBoard(BoardDetailDTO boardDetailDTO) {
        RoomBoard roomBoard = RoomBoard.createEntityFromDTO(boardDetailDTO);

        Map<String, String> data = mapAPI.AddressToCoordinate(boardDetailDTO.getLocation() + " " + boardDetailDTO.getLocationDetail());

        roomBoard.setNewBoard(data);
        Map<String, String> region = mapAPI.CoordinateToRegion(roomBoard.getCordX(), roomBoard.getCordY());

        regionRepository.findByStateAndCityAndTown(
                region.get("region_1depth_name"),
                region.get("region_2depth_name"),
                region.get("region_3depth_name")
        ).ifPresent(r -> {
            roomBoard.setRegion(r);
            r.addBoard(roomBoard);
        });

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
        RoomBoard roomBoard = RoomBoard.createEntityFromDTO(boardDetailDTO);
        roomBoard.setUpdatedAt(LocalDateTime.now());
        RoomBoard save = roomBoardRepository.save(roomBoard);

        return save.getId();
    }

    @Override
    public Page<BoardListDTO> getBoardList(Map<String, String> params, Pageable pageable) {

        Page<BoardListDTO> boardList;

        BigDecimal x = new BigDecimal(params.get("x"));
        BigDecimal y = new BigDecimal(params.get("y"));

        String range = "0.06";
        if (Integer.parseInt(params.get("level")) < 3) {
            range = "0.02";
        }

        boardList = findListAndSort(pageable, x, y, range);

        return boardList;
    }

    private Page<BoardListDTO> findListAndSort(Pageable pageable, BigDecimal x, BigDecimal y, String range) {

        BigDecimal minX = x.subtract(new BigDecimal(range));
        BigDecimal maxX = x.add(new BigDecimal(range));
        BigDecimal minY = y.subtract(new BigDecimal(range));
        BigDecimal maxY = y.add(new BigDecimal(range));

        Page<RoomBoard> boardList;
        boardList = roomBoardRepository.findByCordXBetweenAndCordYBetween(
                minX.toString(),
                maxX.toString(),
                minY.toString(),
                maxY.toString(),
                pageable
        );
        boardList.stream().forEach(board ->
                board.setDistance(x, y)
        );

        Page<BoardListDTO> boardListDTOPage = boardList.map(RoomBoard::toListDTO);
        Stream<BoardListDTO> sorted = boardListDTOPage.stream().sorted(
                Comparator.comparing(BoardListDTO::getDistance)
        );

        return new PageImpl<>(sorted.collect(Collectors.toList()), pageable, boardListDTOPage.getTotalElements());

    }
}