package com.se.kumbangapiserver.domain.board;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Setter

public class RoomBoardSearchQuery {

    private String minCordX;
    private String maxCordX;
    private String minCordY;
    private String maxCordY;
    private String durationType;
    private LocalDate durationStart;
    private LocalDate durationEnd;
    private Integer priceStart;
    private Integer priceEnd;
    private String priceType;
}
