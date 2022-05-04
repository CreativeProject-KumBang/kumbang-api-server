package com.se.kumbangapiserver.dto;

import com.se.kumbangapiserver.domain.archive.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class RegionDetailDTO {
    private Long id;
    private String state;
    private String city;
    private String town;
    private String entx;
    private String enty;
    private String quantity;
    private String price;

}
