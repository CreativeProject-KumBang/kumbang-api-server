package com.se.kumbangapiserver.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponseForm {
    private Boolean status;
    private List<String> response;
}
