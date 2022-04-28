package com.se.kumbangapiserver.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RoomBoardSearchQuery {

    private String entx;
    private String enty;
    private String term;
    private String priceStart;
    private String priceEnd;
    private String priceType;
    private String mapScale;
}
