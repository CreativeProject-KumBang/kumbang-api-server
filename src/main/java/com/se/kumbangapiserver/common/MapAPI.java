package com.se.kumbangapiserver.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MapAPI {

    @Value("${external.map.api.key}")
    private String apiKey;

    public Map<String, String> AddressToCoordinate(String address) {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query="
                + encodedAddress
                + "&page=1&size=1";

        return getCoordinate(getJSONData(url));
    }

    public Map<String, String> CoordinateToAddress(String x, String y) {
        String url = "https://dapi.kakao.com//v2/local/geo/coord2address.json?x="
                + x
                + "&y="
                + y;

        return getAddress(getJSONData(url));
    }

    public Map<String, String> CoordinateToRegion(String x, String y) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x="
                + x
                + "&y="
                + y;
        log.info("x: {} / y: {}", x, y);

        return getRegion(getJSONData(url));
    }

    // Get JSON from Kakao Map API
    public String getJSONData(String url) {
        HttpURLConnection conn;
        StringBuilder json = new StringBuilder();

        String auth = "KakaoAK " + apiKey;

        try {
            URL requestUrl = new URL(url);
            conn = (HttpURLConnection) requestUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Requested-With", "curl");
            conn.setRequestProperty("Authorization", auth);

            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode == 400) {
                throw new Exception("Bad Request");
            } else if (responseCode == 401) {
                throw new Exception("Unauthorized");
            } else if (responseCode == 500) {
                throw new Exception("Server Error");
            } else {
                Charset charset = StandardCharsets.UTF_8;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                String line;
                while ((line = br.readLine()) != null) {
                    json.append(line);
                }
            }
            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getCoordinate(String jsonString) {
        Map<String, String> data = new HashMap<>();
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Integer size = (Integer) meta.get("total_count");

        if (size > 0) {
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject document = (JSONObject) documents.get(0); // 첫 결과 가져옴
            if (document.containsKey("address")) { // 주소가 있는 경우
                addressInsert(data, document);
                data.put("address_x", (String) document.get("x"));
                data.put("address_y", (String) document.get("y"));
            }
            if (document.containsKey("road_address")) { // 도로명 주소가 있는 경우
                roadAddressInsert(data, document);
                data.put("road_address_x", (String) document.get("x"));
                data.put("road_address_y", (String) document.get("y"));
            }
        }
        return data;
    }

    public static Map<String, String> getAddress(String jsonString) {
        Map<String, String> data = new HashMap<>();

        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Integer size = (Integer) meta.get("total_count");

        if (size > 0) {
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject document = (JSONObject) documents.get(0);
            if (document.containsKey("address")) {
                addressInsert(data, document);
            }
            if (document.containsKey("road_address")) {
                roadAddressInsert(data, document);
            }
        }
        return data;
    }

    private static void roadAddressInsert(Map<String, String> data, JSONObject document) {
        JSONObject roadAddress = (JSONObject) document.get("road_address");
        data.put("road_address", "1");
        data.put("road_address_name", (String) roadAddress.get("address_name"));
        data.put("road_address_region_1depth_name", (String) roadAddress.get("region_1depth_name"));
        data.put("road_address_region_2depth_name", (String) roadAddress.get("region_2depth_name"));
        data.put("road_address_region_3depth_name", (String) roadAddress.get("region_3depth_name"));
    }

    private static void addressInsert(Map<String, String> data, JSONObject document) {
        JSONObject address = (JSONObject) document.get("address");
        data.put("address", "1");
        data.put("address_name", (String) address.get("address_name"));
        data.put("address_region_1depth_name", (String) address.get("region_1depth_name"));
        data.put("address_region_2depth_name", (String) address.get("region_2depth_name"));
        data.put("address_region_3depth_name", (String) address.get("region_3depth_name"));
    }

    public Map<String, String> getRegion(String jsonString) {
        Map<String, String> region = new HashMap<>();
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Integer size = (Integer) meta.get("total_count");

        if (size > 0) {
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject document = (JSONObject) documents.get(0);
            if (document.get("region_type").equals("H")) {
                if (size > 1) {
                    document = (JSONObject) documents.get(1);
                }
            }

            region.put("address_name", (String) document.get("address_name"));
            region.put("region_1depth_name", (String) document.get("region_1depth_name"));
            region.put("region_2depth_name", (String) document.get("region_2depth_name"));
            region.put("region_3depth_name", (String) document.get("region_3depth_name"));
            region.put("region_4depth_name", (String) document.get("region_4depth_name"));
            region.put("x", String.valueOf(document.get("x")));
            region.put("y", String.valueOf(document.get("y")));

        }
        return region;
    }
}