package com.se.kumbangapiserver.domain.chat;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatDataRepository extends JpaRepository<ChatData, Long> {

    List<ChatData> findAllByChatRoom(ChatRoom chatRoom);
}
