package com.se.kumbangapiserver.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ResponseForm<T> {
    private Boolean status;
    private T response;
}
