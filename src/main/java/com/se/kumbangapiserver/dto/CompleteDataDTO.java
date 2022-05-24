package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CompleteDataDTO {
    private String id;
    private String contractFee;
    private String contractDeposit;
    private String price;
    private LocalDate startDate;
    private LocalDate endDate;
    private RegionDetailDTO region;
    private String address;
}
