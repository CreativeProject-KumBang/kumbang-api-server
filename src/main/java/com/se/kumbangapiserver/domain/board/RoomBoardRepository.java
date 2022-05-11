package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBoardRepository extends JpaRepository<RoomBoard, Long>, RoomBoardRepositoryCustom {

    Page<RoomBoard> findByCordXBetweenAndCordYBetween(String cordX1, String cordX2, String cordY1, String cordY2, Pageable pageable);

    Page<RoomBoard> findByUser(User user, Pageable pageable);
}
