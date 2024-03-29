package com.se.kumbangapiserver.dto;

import com.se.kumbangapiserver.domain.board.BoardState;
import com.se.kumbangapiserver.domain.board.DurationTerm;
import com.se.kumbangapiserver.domain.board.PriceType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class BoardListDTO {

    private Long id;
    private String title;
    private UserDTO author;
    private BoardState state;
    private Integer hitCount;
    private LocalDate durationStart;
    private LocalDate durationEnd;
    private DurationTerm durationTerm;
    private String location;
    private String locationDetail;
    private Integer price;
    private Integer deposit;
    private PriceType priceType;
    private String entx;
    private String enty;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime removedAt;
    private BigDecimal distance;
    private RegionDetailDTO region;
    private FilesDTO thumbnail;

}
