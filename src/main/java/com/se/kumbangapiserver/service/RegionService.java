package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.RegionDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface RegionService {

    Page<RegionDetailDTO> getRegionAverage(Map<String, String> params, Pageable pageable);
}
