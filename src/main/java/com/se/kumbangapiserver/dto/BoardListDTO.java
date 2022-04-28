package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class BoardListDTO {

    private List<BoardDetailDTO> boardList;
    private MapInformationDTO mapInformation;
}
