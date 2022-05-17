package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.TransactionDataDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DataService {

    Page<TransactionDataDTO> getUserTransactions(String userId, Pageable pageable);

}
