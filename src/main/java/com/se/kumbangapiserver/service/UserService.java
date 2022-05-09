package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.BoardListDTO;

import java.util.List;

public interface UserService {

    List<BoardListDTO> getWishList(String userId);
}
