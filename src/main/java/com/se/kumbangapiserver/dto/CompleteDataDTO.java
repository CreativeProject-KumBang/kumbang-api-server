package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteDataDTO {
    private String id;
    private String contractFee;
    private String contractDeposit;
    private String price;
    private String year;
    private String month;
    private String day;
    private RegionDetailDTO region;
    private String address;

}
