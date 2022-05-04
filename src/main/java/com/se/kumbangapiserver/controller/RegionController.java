package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.dto.RegionDetailDTO;
import com.se.kumbangapiserver.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/api/region/average")
    public ResponseEntity<Page<RegionDetailDTO>> getRegionAverage(
            @RequestParam Map<String, String> params,
            Pageable pageable
    ) {
        try {
            Page<RegionDetailDTO> regionPage = regionService.getRegionAverage(params, pageable);
            return ResponseEntity.ok(regionPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
