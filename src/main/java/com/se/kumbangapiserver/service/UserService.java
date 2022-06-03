package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import com.se.kumbangapiserver.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<BoardListDTO> getWishList();

    UserDTO getUser();

    void updateUser(UserDTO user);

    void deleteUser();

    Page<TransactionDataDTO> getHistory(Pageable pageable);
}
