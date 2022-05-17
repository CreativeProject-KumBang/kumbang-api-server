package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.archive.CompleteTransaction;
import com.se.kumbangapiserver.domain.archive.CompleteTransactionRepository;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import com.se.kumbangapiserver.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final CompleteTransactionRepository transactionRepository;

    @Override
    public Page<TransactionDataDTO> getUserTransactions(String userId, Pageable pageable) {

        Page<CompleteTransaction> transactions = transactionRepository.findAllByUserId(Long.valueOf(userId), pageable);

        return transactions.map(CompleteTransaction::toDTO);
    }
}
