package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.domain.archive.CompleteTransaction;
import com.se.kumbangapiserver.domain.archive.CompleteTransactionRepository;
import com.se.kumbangapiserver.domain.board.WishList;
import com.se.kumbangapiserver.domain.board.WishListRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import com.se.kumbangapiserver.dto.UserDTO;
import com.se.kumbangapiserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final CompleteTransactionRepository completeTransactionRepository;

    @Override
    public List<BoardListDTO> getWishList() {

        User userContext = Common.getUserContext();
        User user = userRepository.findById(userContext.getId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        List<WishList> findWishList = wishListRepository.findAllByUser(user);
        List<BoardListDTO> boardListDTOs = new ArrayList<>();
        for (WishList wishList : findWishList) {

            boardListDTOs.add(wishList.getBoard().toListDTO());
        }

        return boardListDTOs;
    }

    @Override
    public UserDTO getUser() {
        User userContext = Common.getUserContext();
        return userRepository.findById(userContext.getId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다.")).toDTO();
    }

    @Override
    public void updateUser(UserDTO user) {
        User userContext = Common.getUserContext();
        User persistedUser = userRepository.findById(userContext.getId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        persistedUser.setNewInfo(user);
        userRepository.save(persistedUser);
    }

    @Override
    @Transactional
    public void deleteUser() {
        User contextUser = Common.getUserContext();
        User user = userRepository.findById(contextUser.getId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        user.setDeletedAt(LocalDateTime.now());
    }

    @Override
    public Page<TransactionDataDTO> getHistory(Pageable pageable) {
        User userContext = Common.getUserContext();
        User user = userRepository.findById(userContext.getId()).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
        Page<CompleteTransaction> completeTransactions = completeTransactionRepository.findAllByUserId(userContext.getId(), pageable);
        return completeTransactions.map(e -> TransactionDataDTO.builder()
                .id(e.getId())
                .board(e.getRoomBoard().toDetailDTO())
                .region(e.getRegion().toRegionDetailDTO())
                .buyer(e.getBuyer().toDTO())
                .year(e.getYear())
                .month(e.getMonth())
                .day(e.getDay())
                .price(e.getPrice())
                .contractDeposit(e.getContractDeposit())
                .contractFee(e.getContractFee())
                .build());
    }


}
