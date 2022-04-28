package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MarkDTO {

    private String name;
    private String entx;
    private String enty;
    private String dataName;
    private String data;

}
