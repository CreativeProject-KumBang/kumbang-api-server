package com.se.kumbangapiserver.domain.chat;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByRoomBoardAndBuyer(RoomBoard roomBoard, User buyer);

    Page<ChatRoom> findByRoomBoard(RoomBoard roomBoard, Pageable pageable);

}