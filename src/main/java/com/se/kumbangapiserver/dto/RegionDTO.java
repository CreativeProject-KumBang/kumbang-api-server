package com.se.kumbangapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private Long id;
    private String state;
    private String city;
    private String town;
    private String entx;
    private String enty;
    private String quantity;
}
