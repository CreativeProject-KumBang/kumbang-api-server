package com.se.kumbangapiserver.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder

public class TransactionDataDTO {

    private Long id;
    private String address;
    private RegionDetailDTO region;
    private BoardDetailDTO board;
    private UserDTO buyer;
    private String year;
    private String month;
    private String day;
    private String price;
    private String contractDeposit;
    private String contractFee;
}
