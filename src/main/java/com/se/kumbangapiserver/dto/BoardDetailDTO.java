package com.se.kumbangapiserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.se.kumbangapiserver.domain.board.BoardState;
import com.se.kumbangapiserver.domain.board.Details;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor

public class BoardDetailDTO {

    private Long boardId;
    private String title;
    private String content;
    private UserDTO user;
    private int hitCount;
    private BoardState state;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate durationStart;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate durationEnd;

    private String location;
    private String locationDetail;
    private int contractDeposit;
    private int contractMonthlyFee;
    private String fixedOption;
    private String additionalOption;
    private Double cordX;
    private Double cordY;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime removedAt;

    private Double distance;
    private Details details;

    private List<FileDTO> files;

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }
}
