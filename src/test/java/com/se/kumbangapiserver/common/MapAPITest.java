package com.se.kumbangapiserver.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapAPITest {

    @Autowired
    MapAPI mapAPI;

    @Test
    void getCoordinate() {

        List<String> strings = mapAPI.AddressToCoordinate("경상북도 구미시 대학로 61");
        assertThat(strings.get(0)).isEqualTo("128.387881880598");
        assertThat(strings.get(1)).isEqualTo("36.1456315429562");

    }

    @Test
    void getCoordinate2() {
        List<String> strings = mapAPI.AddressToCoordinate("부산광역시 북구 효열로 195");
        assertThat(strings.get(0)).isEqualTo("129.017892119046");
        assertThat(strings.get(1)).isEqualTo("35.2621831879272");
    }

    @Test
    void getAddress() {
        String address = mapAPI.CoordinateToAddress("128.387881880598", "36.1456315429562");
        assertThat(address).isEqualTo("경상북도 구미시 대학로 61");
    }

    @Test
    void getAddress2() {
        String address = mapAPI.CoordinateToAddress("129.017892119046", "35.2621831879272");
        assertThat(address).isEqualTo("부산광역시 북구 효열로 195");
    }
}