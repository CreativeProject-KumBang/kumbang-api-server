package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.dto.RegionDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByStateAndCityAndTown(String state, String city, String town);

    Page<Region> findByAvgEntxBetweenAndAvgEntyBetween(BigDecimal avgEntx1, BigDecimal avgEntx2, BigDecimal avgEnty1, BigDecimal avgEnty2, Pageable pageable);

    @Query(value =
            "select " +
                    "new com.se.kumbangapiserver.dto.RegionDetailDTO(" +
                    "sum(r.entx), sum(r.enty),sum( r.quantity), r.state, r.city, sum(r.totalPrice))"
                    + " from Region r " +
                    "group by r.state, r.city" +
                    " having sum(r.quantity) > 0 ")
    List<RegionDetailDTO> findGroupByCity(String avgEntx1, String avgEntx2, String avgEnty1, String avgEnty2, Pageable pageable);
}
