package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@Builder
public class CompleteDataDTO {
    private String id;
    private String contractFee;
    private String contractDeposit;
    private UserDTO buyer;
    private String price;
    private String deposit;
    private LocalDate startDate;
    private LocalDate endDate;
    private RegionDetailDTO region;
    private String address;
}
