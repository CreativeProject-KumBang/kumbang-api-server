package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.common.MapAPI;
import com.se.kumbangapiserver.domain.archive.CompleteTransaction;
import com.se.kumbangapiserver.domain.archive.CompleteTransactionRepository;
import com.se.kumbangapiserver.domain.archive.Region;
import com.se.kumbangapiserver.domain.archive.RegionRepository;
import com.se.kumbangapiserver.domain.board.*;
import com.se.kumbangapiserver.domain.chat.ChatRoom;
import com.se.kumbangapiserver.domain.chat.ChatRoomRepository;
import com.se.kumbangapiserver.domain.file.FileRepository;
import com.se.kumbangapiserver.domain.file.Files;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.*;
import com.se.kumbangapiserver.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final RoomBoardRepository roomBoardRepository;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final CompleteTransactionRepository completeTransactionRepository;
    private final FileRepository fileRepository;
    private final BoardFilesRepository boardFilesRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MapAPI mapAPI;


    private final WishListRepository wishlistRepository;

    @Override
    public BoardDetailDTO getBoardDetail(String boardId) {
        Optional<RoomBoard> findBoard = roomBoardRepository.findById(Long.valueOf(boardId));
        BoardDetailDTO boardDetailDTO;
        if (findBoard.isPresent()) {
            boardDetailDTO = findBoard.get().toDetailDTO();
            List<CompleteDataDTO> collect = completeTransactionRepository
                    .findAllByAddressOrderByCreatedAtDesc(boardDetailDTO.getLocation())
                    .stream()
                    .map(CompleteTransaction::toCompleteDTO)
                    .collect(Collectors.toList());
            boardDetailDTO.setCompleteData(collect);
            return boardDetailDTO;
        }
        throw new RuntimeException("Board not found");
    }

    @Override
    @Transactional
    public Long createBoard(BoardDetailDTO boardDetailDTO) {
        boardDetailDTO.setUser(Common.getUserContext().toDTO());

        RoomBoard roomBoard = RoomBoard.createEntityFromDTO(boardDetailDTO);

        Map<String, String> data = mapAPI.AddressToCoordinate(boardDetailDTO.getLocation());

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
        List<BoardFiles> files = roomBoard.getFiles();
        if (boardDetailDTO.getFiles() != null) {
            boardDetailDTO.getFiles().forEach(i -> {
                        Optional<Files> image = fileRepository.findById(i.getId());
                        if (image.isEmpty()) {
                            return;
                        }
                        BoardFiles boardFiles = BoardFiles.makeRelation(roomBoard, image.get());
                        files.add(boardFiles);
                        boardFilesRepository.save(boardFiles);
                    }
            );
        }
        long between = ChronoUnit.DAYS.between(
                boardDetailDTO.getDurationStart(),
                boardDetailDTO.getDurationEnd());

        if (between > 30) {
            roomBoard.setDurationTerm(DurationTerm.LONG);
        } else {
            roomBoard.setDurationTerm(DurationTerm.SHORT);
        }


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

        boardList = findListAndSort(params, pageable);

        return boardList;
    }

    @Override
    public Boolean isLike(Map<String, String> params) {

        User contextUser = Common.getUserContext();
        User persistedUser = userRepository.findById(contextUser.getId()).orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
        return persistedUser.getWishLists().stream().anyMatch(wishList -> wishList.getBoard().getId().equals(Long.valueOf(params.get("boardId"))));
    }

    @Override
    @Transactional
    public Boolean like(@RequestBody Map<String, String> params) {

        Long contextUser = Common.getUserContext().getId();
        User persistedUser = userRepository.findById(contextUser).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(params.get("boardId"))).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Optional<WishList> wishlist = wishlistRepository.findByUserIdAndBoardId(persistedUser.getId(), roomBoard.getId());

        if (wishlist.isPresent()) {
            return false;
        } else {
            WishList build = WishList.builder().user(persistedUser).board(roomBoard).build();
            wishlistRepository.save(build);
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean unlike(@RequestBody Map<String, String> params) {

        Long contextUser = Common.getUserContext().getId();
        User persistedUser = userRepository.findById(contextUser).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(params.get("boardId"))).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Optional<WishList> wishlist = wishlistRepository.findByUserIdAndBoardId(persistedUser.getId(), roomBoard.getId());

        if (wishlist.isPresent()) {
            wishlistRepository.delete(wishlist.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean complete(String boardId, CompleteDataDTO completeDataDTO) {

        RoomBoard roomBoard = roomBoardRepository.findById(Long.valueOf(boardId)).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        if (roomBoard.getState().equals(BoardState.CLOSED)) {
            throw new RuntimeException("이미 완료된 게시글입니다.");
        }

        User buyer = userRepository.findById(Long.valueOf(completeDataDTO.getBuyer().getId())).orElseThrow(() -> new RuntimeException("구매자 정보를 찾을 수 없습니다."));

        LocalDateTime now = LocalDateTime.now();

        roomBoard.setState(BoardState.CLOSED);
        roomBoard.setUpdatedAt(now);
        roomBoard.setCompleteData(completeDataDTO);

        roomBoardRepository.save(roomBoard);

        Region region = regionRepository.findById(roomBoard.getRegion().getId()).orElseThrow(() -> new RuntimeException("존재하지 않는 지역입니다."));
        region.removeBoard(roomBoard);

        CompleteTransaction trans = CompleteTransaction.builder()
                .address(roomBoard.getLocation())
                .createdAt(now)
                .updatedAt(now)
                .region(roomBoard.getRegion())
                .year(String.valueOf(now.getYear()))
                .month(String.valueOf(now.getMonthValue()))
                .day(String.valueOf(now.getDayOfMonth()))
                .startDate(roomBoard.getDurationStart())
                .deposit(String.valueOf(roomBoard.getDeposit()))
                .endDate(roomBoard.getDurationEnd())
                .price(String.valueOf(roomBoard.getPrice()))
                .contractDeposit(String.valueOf(roomBoard.getContractDeposit()))
                .contractFee(String.valueOf(roomBoard.getContractMonthlyFee()))
                .roomBoard(roomBoard)
                .build();

        trans.setBuyer(buyer);

        completeTransactionRepository.save(trans);

        return true;
    }

    @Override
    public Page<BoardListDTO> getMyBoardList(String userId, Pageable pageable) {

        User contextUser = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new RuntimeException("유저 정보를 찾을 수 없습니다."));
        Page<RoomBoard> findBoards = roomBoardRepository.findByUser(contextUser, pageable);

        return findBoards.map(RoomBoard::toListDTO);
    }

    @Override
    public Page<UserDTO> getBuyerList(Long boardId, Pageable pageable) {
        RoomBoard roomBoard = roomBoardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        Page<ChatRoom> chatRoomPage = chatRoomRepository.findAllByRoomBoard(roomBoard, pageable);

        return chatRoomPage.map(ChatRoom::getBuyer).map(User::toDTO);
    }

    private Page<BoardListDTO> findListAndSort(Map<String, String> params, Pageable pageable) {

        BigDecimal x = new BigDecimal(params.get("x"));
        BigDecimal y = new BigDecimal(params.get("y"));

        String range = "0.06";
        if (Integer.parseInt(params.get("level")) < 3) {
            range = "0.02";
        }

        BigDecimal minX = x.subtract(new BigDecimal(range));
        BigDecimal maxX = x.add(new BigDecimal(range));
        BigDecimal minY = y.subtract(new BigDecimal(range));
        BigDecimal maxY = y.add(new BigDecimal(range));

        List<RoomBoard> boardList;

        RoomBoardSearchQuery queryParams = RoomBoardSearchQuery.builder()
                .minCordX(String.valueOf(minX))
                .maxCordX(String.valueOf(maxX))
                .minCordY(String.valueOf(minY))
                .maxCordY(String.valueOf(maxY))
                .build();

        if (params.containsKey("durationType")) {
            queryParams.setDurationType(params.get("durationType"));
        }
        if (params.containsKey("startDate")) {
            queryParams.setDurationStart(LocalDate.parse(params.get("startDate")));
        }
        if (params.containsKey("endDate")) {
            queryParams.setDurationEnd(LocalDate.parse(params.get("endDate")));
        }
        if (params.containsKey("priceStart")) {
            queryParams.setPriceStart(Integer.parseInt(params.get("priceStart")));
        }
        if (params.containsKey("priceEnd")) {
            queryParams.setPriceEnd(Integer.parseInt(params.get("priceEnd")));
        }


        boardList = roomBoardRepository.findByDynamicQuery(queryParams);

        boardList.forEach(board ->
                board.setDistance(x, y)
        );

        boardList.sort(Comparator.comparing(RoomBoard::getDistance));
        List<BoardListDTO> dtoList = boardList.stream().map(RoomBoard::toListDTO).collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, dtoList.size());

    }
}