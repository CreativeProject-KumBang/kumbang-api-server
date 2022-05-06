package com.se.kumbangapiserver.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList> findByUserIdAndBoardId(Long userId, Long boardId);
}
