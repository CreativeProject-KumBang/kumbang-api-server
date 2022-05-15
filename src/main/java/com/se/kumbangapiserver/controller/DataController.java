package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import com.se.kumbangapiserver.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DataController {

    private final DataService dataService;

    @GetMapping("/api/data/board/{id}")
    public ResponseEntity<ResponseForm<Object>> getTransactionData(@PathVariable String id) {

        dataService.getTransactionData(id);

        return null;
    }

}
