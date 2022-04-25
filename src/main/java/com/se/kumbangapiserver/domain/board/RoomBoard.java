package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.FileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "room_board")
public class RoomBoard extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "board_state")
    @Enumerated(EnumType.STRING)
    private BoardState state = BoardState.OPEN;

    @Column(name = "hit_count")
    private int hitCount = 0;

    @Column(name = "duration_start")
    private LocalDate durationStart;

    @Column(name = "duration_end")
    private LocalDate durationEnd;

    @Column(name = "location")
    private String location;

    @Column(name = "location_detail")
    private String locationDetail;

    @Column(name = "contract_deposit")
    private int contractDeposit;

    @Column(name = "contract_monthly_fee")
    private int contractMonthlyFee;

    @Column(name = "fixed_option")
    private String fixedOption;

    @Column(name = "additional_option")
    private String additionalOption;

    @Builder.Default
    @Column(name = "cord_x")
    private Double cordX = 0.0;

    @Builder.Default
    @Column(name = "cord_y")
    private Double cordY = 0.0;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Builder.Default
    @Transient
    private Double distance = 0.0;

    @Embedded
    private Details details;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomBoard", orphanRemoval = true)
    private List<BoardFiles> files = new ArrayList<>();

    public List<BoardFiles> getBoardFiles() {
        return files;
    }

    public Long getId() {
        return id;
    }

    public void changeState(BoardState state) {
        this.state = state;
    }

    public static RoomBoard fromDTO(BoardDetailDTO boardDetailDTO) {

        RoomBoard roomBoard = RoomBoard.builder()
                .id(boardDetailDTO.getBoardId())
                .title(boardDetailDTO.getTitle())
                .content(boardDetailDTO.getContent())
                .user(User.fromDTO(boardDetailDTO.getUser()))
                .state(boardDetailDTO.getState())
                .hitCount(boardDetailDTO.getHitCount())
                .durationStart(boardDetailDTO.getDurationStart())
                .durationEnd(boardDetailDTO.getDurationEnd())
                .location(boardDetailDTO.getLocation())
                .locationDetail(boardDetailDTO.getLocationDetail())
                .contractDeposit(boardDetailDTO.getContractDeposit())
                .contractMonthlyFee(boardDetailDTO.getContractMonthlyFee())
                .fixedOption(boardDetailDTO.getFixedOption())
                .additionalOption(boardDetailDTO.getAdditionalOption())
                .cordX(boardDetailDTO.getCordX())
                .cordY(boardDetailDTO.getCordY())
                .removedAt(boardDetailDTO.getRemovedAt())
                .distance(boardDetailDTO.getDistance())
                .details(boardDetailDTO.getDetails())
                .build();

        if (boardDetailDTO.getFiles() != null) {
            List<BoardFiles> boardFiles = roomBoard.getBoardFiles();
            for (FileDTO fileDTO : boardDetailDTO.getFiles()) {
                boardFiles.add(BoardFiles.makeRelation(roomBoard, File.fromDTO(fileDTO)));
            }
        }

        return roomBoard;
    }

    public BoardDetailDTO toDTO() {

        BoardDetailDTO boardDetailDTO = BoardDetailDTO.builder()
                .boardId(this.id)
                .title(this.title)
                .content(this.content)
                .user(this.user.toDTO())
                .state(this.state)
                .hitCount(this.hitCount)
                .durationStart(this.durationStart)
                .durationEnd(this.durationEnd)
                .location(this.location)
                .locationDetail(this.locationDetail)
                .contractDeposit(this.contractDeposit)
                .contractMonthlyFee(this.contractMonthlyFee)
                .fixedOption(this.fixedOption)
                .additionalOption(this.additionalOption)
                .cordX(this.cordX)
                .cordY(this.cordY)
                .removedAt(this.removedAt)
                .distance(this.distance)
                .details(this.details)
                .build();

        if (this.files != null) {
            List<FileDTO> fileDTOs = new ArrayList<>();
            for (BoardFiles boardFiles : this.files) {
                fileDTOs.add(boardFiles.getFile().toDTO());
            }
            boardDetailDTO.setFiles(fileDTOs);
        }

        return boardDetailDTO;
    }
}
