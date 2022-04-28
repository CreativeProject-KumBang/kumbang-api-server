package com.se.kumbangapiserver.domain.archive;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    Optional<Region> findByStateAndCityAndTown(String state, String city, String town);
}
