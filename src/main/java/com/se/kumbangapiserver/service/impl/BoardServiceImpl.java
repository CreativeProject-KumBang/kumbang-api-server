package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.MapAPI;
import com.se.kumbangapiserver.domain.archive.CompleteTransaction;
import com.se.kumbangapiserver.domain.archive.CompleteTransactionRepository;
import com.se.kumbangapiserver.domain.archive.Region;
import com.se.kumbangapiserver.domain.archive.RegionRepository;
import com.se.kumbangapiserver.domain.board.*;
import com.se.kumbangapiserver.domain.file.FileRepository;
import com.se.kumbangapiserver.domain.file.Files;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
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
    private final UserRepository userRepository;
    private final CompleteTransactionRepository completeTransactionRepository;
    private final FileRepository fileRepository;
    private final BoardFilesRepository boardFilesRepository;
    private final MapAPI mapAPI;


    private final WishListRepository wishlistRepository;

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

        boardDetailDTO.getImages().forEach(i -> {
                    Optional<Files> image = fileRepository.findById(Long.valueOf(i));
                    if (image.isEmpty()) {
                        return;
                    }
                    BoardFiles boardFiles = BoardFiles.makeRelation(roomBoard, image.get());
                    boardFilesRepository.save(boardFiles);
                }
        );

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

    @Override
    public Boolean isLike(Map<String, String> params) {

        User contextUser = Common.getUserContext();
        User persistedUser = userRepository.findById(contextUser.getId()).orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
        return persistedUser.getWishLists().stream().anyMatch(wishList -> wishList.getBoard().getId().equals(Long.valueOf(params.get("boardId"))));
    }

    @Override
    public Boolean like(Map<String, String> params) {

        User contextUser = Common.getUserContext();
        User persistedUser = userRepository.findById(contextUser.getId()).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(params.get("boardId"))).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Optional<WishList> wishlist = wishlistRepository.findByUserIdAndBoardId(contextUser.getId(), roomBoard.getId());

        if (wishlist.isPresent()) {
            return false;
        } else {
            WishList build = WishList.builder().user(persistedUser).board(roomBoard).build();
            wishlistRepository.save(build);
            return true;
        }
    }

    @Override
    public Boolean unlike(Map<String, String> params) {

        User contextUser = Common.getUserContext();
        User persistedUser = userRepository.findById(contextUser.getId()).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(params.get("boardId"))).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Optional<WishList> wishlist = wishlistRepository.findByUserIdAndBoardId(contextUser.getId(), roomBoard.getId());

        if (wishlist.isPresent()) {
            wishlistRepository.delete(wishlist.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean complete(String boardId, CompleteDataDTO completeDataDTO) {

        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(boardId)).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        if (roomBoard.getState().equals(BoardState.CLOSED)) {
            return false;
        }
        roomBoard.setState(BoardState.CLOSED);
        roomBoard.setUpdatedAt(LocalDateTime.now());
        roomBoard.setCompleteData(completeDataDTO);

        roomBoardRepository.save(roomBoard);

        Region region = regionRepository.findById(roomBoard.getRegion().getId()).orElseThrow(() -> new RuntimeException("존재하지 않는 지역입니다."));
        region.removeBoard(roomBoard);

        LocalDateTime now = LocalDateTime.now();

        CompleteTransaction trans = CompleteTransaction.builder()
                .address(roomBoard.getLocation())
                .region(roomBoard.getRegion())
                .year(String.valueOf(now.getYear()))
                .month(String.valueOf(now.getMonthValue()))
                .day(String.valueOf(now.getDayOfMonth()))
                .price(String.valueOf(roomBoard.getPrice()))
                .contractDeposit(String.valueOf(roomBoard.getContractDeposit()))
                .contractFee(String.valueOf(roomBoard.getContractMonthlyFee()))
                .roomBoard(roomBoard)
                .build();

        completeTransactionRepository.save(trans);

        return true;
    }

    @Override
    public Page<BoardListDTO> getMyBoardList(String userId, Pageable pageable) {

        User contextUser = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        Page<RoomBoard> findBoards = roomBoardRepository.findByUser(contextUser, pageable);

        return findBoards.map(RoomBoard::toListDTO);
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