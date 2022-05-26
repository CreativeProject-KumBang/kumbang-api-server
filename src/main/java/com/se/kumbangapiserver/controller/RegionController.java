package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.RegionDetailDTO;
import com.se.kumbangapiserver.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/api/region/average")
    public ResponseEntity<ResponseForm<Object>> getRegionAverage(
            @RequestParam Map<String, String> params,
            Pageable pageable
    ) {
        try {
            for (String key : params.keySet()) {
                log.info("params key : {} / param value : {}", key, params.get(key));
            }
            Page<RegionDetailDTO> regionPage = regionService.getRegionAverage(params, pageable);
            return ResponseEntity.ok(ResponseForm.builder().status(true).response(Collections.singletonList(regionPage)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(false).response(Collections.singletonList(e.getMessage())).build());
        }
    }
}
