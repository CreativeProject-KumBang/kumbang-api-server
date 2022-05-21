package com.se.kumbangapiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.se.kumbangapiserver.domain.board.BoardState;
import com.se.kumbangapiserver.domain.board.Details;
import com.se.kumbangapiserver.domain.board.DurationTerm;
import com.se.kumbangapiserver.domain.board.PriceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@ToString
public class BoardDetailDTO {

    private Long boardId;
    private String title;
    private String content;
    private UserDTO user;
    private Integer hitCount;
    private BoardState state;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate durationStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate durationEnd;

    private DurationTerm durationTerm;

    private String location;
    private String locationDetail;
    private Integer contractDeposit;
    private Integer contractMonthlyFee;
    private Integer price;
    private Integer deposit;
    private List<String> fixedOption;
    private String additionalOption;
    private String cordX;
    private String cordY;

    private RegionDetailDTO region;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime removedAt;

    private BigDecimal distance;
    private Details details;

    private List<FilesDTO> files;

    public void setFiles(List<FilesDTO> files) {
        this.files = files;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
