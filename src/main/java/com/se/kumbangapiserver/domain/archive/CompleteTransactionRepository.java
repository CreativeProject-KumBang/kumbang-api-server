package com.se.kumbangapiserver.domain.archive;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompleteTransactionRepository extends JpaRepository<CompleteTransaction, Long> {

    List<CompleteTransaction> findAllByAddress(String address);
}
