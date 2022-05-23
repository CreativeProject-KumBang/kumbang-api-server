package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.archive.Region;
import com.se.kumbangapiserver.domain.archive.RegionRepository;
import com.se.kumbangapiserver.dto.RegionDetailDTO;
import com.se.kumbangapiserver.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        BigDecimal ranges = new BigDecimal(range);
        BigDecimal minX = x.subtract(ranges);
        BigDecimal maxX = x.add(ranges);
        BigDecimal minY = y.subtract(ranges);
        BigDecimal maxY = y.add(ranges);

        if (ranges.compareTo(new BigDecimal("1")) >= 0) {
            List<RegionDetailDTO> groupByCity = regionRepository.findGroupByCity(
                    minX.toString(),
                    maxX.toString(),
                    minY.toString(),
                    maxY.toString(),
                    pageable
            );
            for (RegionDetailDTO regionDetailDTO : groupByCity) {
                log.info("{}", regionDetailDTO.toString());
            }

            List<RegionDetailDTO> cityList = groupByCity.stream().peek(e -> {
                BigDecimal entx = e.getEntx();
                BigDecimal enty = e.getEnty();
                BigDecimal quantity = new BigDecimal(e.getQuantity());
                BigDecimal price = e.getPrice();
                e.setEntx(entx.divide(quantity, 10, RoundingMode.HALF_UP));
                e.setEnty(enty.divide(quantity, 10, RoundingMode.HALF_UP));
                e.setPrice(price.divide(quantity, 10, RoundingMode.HALF_UP));
            }).collect(Collectors.toList());

            return new PageImpl<>(cityList, pageable, cityList.size());
        }

        return regionRepository.findByAvgEntxBetweenAndAvgEntyBetween(
                minX,
                maxX,
                minY,
                maxY,
                pageable
        ).map(Region::toRegionDetailDTO);
    }

}
