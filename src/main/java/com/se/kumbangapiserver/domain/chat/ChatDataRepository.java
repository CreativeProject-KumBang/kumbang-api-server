package com.se.kumbangapiserver.domain.chat;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatDataRepository extends JpaRepository<ChatData, Long> {
}
