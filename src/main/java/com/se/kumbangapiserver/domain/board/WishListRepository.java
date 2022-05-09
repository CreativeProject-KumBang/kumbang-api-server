package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserIdAndBoardId(Long userId, Long boardId);

    List<WishList> findAllByUser(User user);
}
