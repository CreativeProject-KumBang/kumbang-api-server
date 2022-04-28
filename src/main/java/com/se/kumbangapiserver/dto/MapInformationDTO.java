package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter

public class MapInformationDTO {

    private Integer level;
    private List<MarkDTO> marks;
}
