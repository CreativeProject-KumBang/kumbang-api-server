package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<BoardListDTO> getWishList(String userId);

    UserDTO getUser(String userId);

    void updateUser(UserDTO user);

    void deleteUser();
}
