package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.archive.Region;
import com.se.kumbangapiserver.domain.archive.RegionRepository;
import com.se.kumbangapiserver.dto.RegionDetailDTO;
import com.se.kumbangapiserver.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;

    @Override
    public Page<RegionDetailDTO> getRegionAverage(Map<String, String> params, Pageable pageable) {

        BigDecimal x = new BigDecimal(params.get("x"));
        BigDecimal y = new BigDecimal(params.get("y"));
        String range = "2";

        if (Integer.parseInt(params.get("level")) < 7) {
            range = "0.25";
        } else if (Integer.parseInt(params.get("level")) < 9) {
            range = "0.5";
        } else if (Integer.parseInt(params.get("level")) < 11) {
            range = "1";
        }

        return findList(pageable, x, y, range);
    }

    private Page<RegionDetailDTO> findList(Pageable pageable, BigDecimal x, BigDecimal y, String range) {
        BigDecimal minX = x.subtract(new BigDecimal(range));
        BigDecimal maxX = x.add(new BigDecimal(range));
        BigDecimal minY = y.subtract(new BigDecimal(range));
        BigDecimal maxY = y.add(new BigDecimal(range));

        return regionRepository.findByAvgEntxBetweenAndAvgEntyBetween(
                minX.toString(),
                maxX.toString(),
                minY.toString(),
                maxY.toString(),
                pageable
        ).map(Region::toRegionDetailDTO);
    }

}
