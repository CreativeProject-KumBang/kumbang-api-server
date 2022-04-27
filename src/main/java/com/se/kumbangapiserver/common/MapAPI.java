package com.se.kumbangapiserver.common;

import lombok.RequiredArgsConstructor;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class MapAPI {

    @Value("${external.map.api.key}")
    private String apiKey;

    public List<String> AddressToCoordinate(String address) {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String url = "https://dapi.kakao.com/v2/local/search/address.json?query="
                + encodedAddress
                + "&page=1&size=1";

        return getCoordinate(getJSONData(url));
    }

    public String CoordinateToAddress(String x, String y) {
        String url = "https://dapi.kakao.com//v2/local/geo/coord2address.json?x="
                + x
                + "&y="
                + y;

        return getAddress(getJSONData(url));
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

    public static List<String> getCoordinate(String jsonString) {
        String coordinate_x = "";
        String coordinate_y = "";
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Integer size = (Integer) meta.get("total_count");

        if (size > 0) {
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject document = (JSONObject) documents.get(0);
            coordinate_x = (String) document.get("x");
            coordinate_y = (String) document.get("y");
        }
        return List.of(coordinate_x, coordinate_y);
    }

    public static String getAddress(String jsonString) {
        String address = "";
        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject meta = (JSONObject) jsonObject.get("meta");
        Integer size = (Integer) meta.get("total_count");

        if (size > 0) {
            JSONArray documents = (JSONArray) jsonObject.get("documents");
            JSONObject document = (JSONObject) documents.get(0);
            JSONObject roadAddress = (JSONObject) document.get("road_address");

            if (roadAddress == null) {
                JSONObject subObject = (JSONObject) document.get("address");
                address = (String) subObject.get("address_name");
            } else {
                address = (String) roadAddress.get("address_name");
            }
        }
        return address;
    }
}