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
import java.util.Objects;

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
    @Builder.Default
    private BoardState state = BoardState.OPEN;

    @Column(name = "hit_count")
    @Builder.Default
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

    @Column(name = "cord_x")
    private String cordX;

    @Column(name = "cord_y")
    private String cordY;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    @Builder.Default
    @Transient
    private Double distance = 0.0;

    @Embedded
    private Details details;

    @Builder.Default
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

    public void setRemoved() {
        this.removedAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setNewBoard(List<String> coordinate) {
        this.state = BoardState.OPEN;
        hitCount = 0;
        if (!Objects.equals(coordinate.get(0), "") && !Objects.equals(coordinate.get(1), "")) {
            this.cordX = coordinate.get(0);
            this.cordY = coordinate.get(1);
        } else {
            this.cordX = "0";
            this.cordY = "0";
        }
        LocalDateTime now = LocalDateTime.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    public void setCoordinate(String cordX, String cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
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
