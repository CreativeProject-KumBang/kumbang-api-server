package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompleteTransactionRepository extends JpaRepository<CompleteTransaction, Long> {

    List<CompleteTransaction> findAllByAddressOrderByCreatedAtDesc(String address);

    Optional<CompleteTransaction> findByRoomBoard(RoomBoard roomBoard);

    @Query(value = "SELECT * from complete_transaction where buyer_id = ?1 or board_id = (select id from room_board where user_id = ?1)", nativeQuery = true)
    Page<CompleteTransaction> findAllByUserId(Long userId, Pageable pageable);

}
