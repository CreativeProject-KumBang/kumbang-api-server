package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.common.Common;
import com.se.kumbangapiserver.domain.board.WishList;
import com.se.kumbangapiserver.domain.board.WishListRepository;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.domain.user.UserRepository;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;

    @Override
    public List<BoardListDTO> getWishList(String userId) {

        User userContext = Common.getUserContext();
        List<WishList> findWishList = wishListRepository.findAllByUser(userContext);
        List<BoardListDTO> boardListDTOs = new ArrayList<>();
        for (WishList wishList : findWishList) {

            boardListDTOs.add(wishList.getBoard().toListDTO());
        }

        return boardListDTOs;
    }
}