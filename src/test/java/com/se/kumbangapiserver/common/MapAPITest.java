package com.se.kumbangapiserver.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapAPITest {

    @Autowired
    MapAPI mapAPI;

    @Test
    void getCoordinate() {

        List<String> strings = mapAPI.AddressToCoordinate("%EA%B2%BD%EC%83%81%EB%B6%81%EB%8F%84%20%EA%B5%AC%EB%AF%B8%EC%8B%9C%20%EB%8C%80%ED%95%99%EB%A1%9C%2061%EA%B8%B8");
        assertEquals(strings.get(0), "128.387881880598");
        assertEquals(strings.get(1), "36.1456315429562");

    }

    @Test
    void getCoordinate2() {
        List<String> strings = mapAPI.AddressToCoordinate("%EB%B6%80%EC%82%B0%EA%B4%91%EC%97%AD%EC%8B%9C%20%ED%9A%A8%EC%97%B4%EB%A1%9C%20195%EA%B8%B8");
        assertEquals(strings.get(0), "129.017892119046");
        assertEquals(strings.get(1), "35.2621831879272");
    }

    @Test
    void getAddress() {
        String address = mapAPI.CoordinateToAddress("128.387881880598", "36.1456315429562");
        assertEquals(address, "경상북도 구미시 대학로 61");
    }

    @Test
    void getAddress2() {
        String address = mapAPI.CoordinateToAddress("129.017892119046", "35.2621831879272");
        assertEquals(address, "부산광역시 북구 효열로 195");
    }
}