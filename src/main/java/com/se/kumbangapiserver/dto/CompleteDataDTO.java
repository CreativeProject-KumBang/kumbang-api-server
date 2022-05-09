package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteDataDTO {
    private String id;
    private String contractPrice;
    private String contractDate;
}
