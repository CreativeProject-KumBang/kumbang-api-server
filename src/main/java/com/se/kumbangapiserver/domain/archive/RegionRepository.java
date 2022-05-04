package com.se.kumbangapiserver.domain.archive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByStateAndCityAndTown(String state, String city, String town);

    Page<Region> findByAvgEntxBetweenAndAvgEntyBetween(String avgEntx1, String avgEntx2, String avgEnty1, String avgEnty2, Pageable pageable);

}
