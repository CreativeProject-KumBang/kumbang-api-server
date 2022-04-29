package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.BoardListDTO;

import java.util.Map;

public interface RegionService {

    BoardListDTO getRegionAvg(Map<String, String> params);
}
