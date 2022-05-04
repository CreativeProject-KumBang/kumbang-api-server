package com.se.kumbangapiserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FilesDTO {

    private Long id;
    private String name;
    private String path;
    private String size;
    private String type;
}
