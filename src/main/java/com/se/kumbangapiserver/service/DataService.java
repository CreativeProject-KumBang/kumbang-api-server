package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.TransactionDataDTO;

import java.util.List;

public interface DataService {

    List<TransactionDataDTO> getTransactionData(String transactionId);
}
