package com.se.kumbangapiserver.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapAPITest {

    @Autowired
    MapAPI mapAPI;

    @Test
    void getCoordinate() {

        Map<String, String> data = mapAPI.AddressToCoordinate("경상북도 구미시 대학로 61");
        if (data.containsKey("road_address")) {
            assertThat(data.get("road_address_x")).isEqualTo("128.387881880598");
            assertThat(data.get("road_address_y")).isEqualTo("36.1456315429562");
        } else if (data.containsKey("address")) {
            assertThat(data.get("address_x")).isEqualTo("128.387881880598");
            assertThat(data.get("address_y")).isEqualTo("36.1456315429562");
        }


    }

    @Test
    void getCoordinate2() {
        Map<String, String> data = mapAPI.AddressToCoordinate("부산광역시 북구 효열로 195");
        if (data.containsKey("road_address")) {
            assertThat(data.get("road_address_x")).isEqualTo("129.017892119046");
            assertThat(data.get("road_address_y")).isEqualTo("35.2621831879272");
        } else if (data.containsKey("address")) {
            assertThat(data.get("address_x")).isEqualTo("129.017892119046");
            assertThat(data.get("address_y")).isEqualTo("35.2621831879272");
        }

    }

    @Test
    void getAddress() {
        Map<String, String> data = mapAPI.CoordinateToAddress("128.387881880598", "36.1456315429562");
        if (data.containsKey("road_address")) {
            assertThat(data.get("road_address_name")).isEqualTo("경상북도 구미시 대학로 61");
        } else if (data.containsKey("address")) {
            assertThat(data.get("address_name")).isEqualTo("경상북도 구미시 대학로 61");
        }
    }

    @Test
    void getAddress2() {
        Map<String, String> data = mapAPI.CoordinateToAddress("129.017892119046", "35.2621831879272");
        if (data.containsKey("road_address")) {
            assertThat(data.get("road_address_name")).isEqualTo("부산광역시 북구 효열로 195");
        } else if (data.containsKey("address")) {
            assertThat(data.get("address_name")).isEqualTo("부산광역시 북구 효열로 195");
        }
    }
}