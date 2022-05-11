package com.se.kumbangapiserver.domain.board;

import com.se.kumbangapiserver.domain.archive.CompleteTransaction;
import com.se.kumbangapiserver.domain.archive.Region;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.file.Files;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.BoardDetailDTO;
import com.se.kumbangapiserver.dto.BoardListDTO;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import com.se.kumbangapiserver.dto.FilesDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Integer hitCount = 0;

    @Column(name = "duration_start")
    private LocalDate durationStart;

    @Column(name = "duration_end")
    private LocalDate durationEnd;

    @Column(name = "location")
    private String location;

    @Column(name = "location_detail")
    private String locationDetail;

    @Column(name = "contract_deposit")
    private Integer contractDeposit;

    @Column(name = "contract_monthly_fee")
    private Integer contractMonthlyFee;

    @Column(name = "price")
    private Integer price;

    @Column(name = "price_type")
    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Column(name = "deposit")
    private Integer deposit;


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

    @Transient
    private BigDecimal distance;

    @Embedded
    private Details details;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roomBoard", orphanRemoval = true)
    private List<BoardFiles> files;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    private Region region;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "roomBoard", cascade = CascadeType.PERSIST)
    private CompleteTransaction completeTransaction;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "board", orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<WishList> wishList;

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

    public Integer getPrice() {
        return price;
    }

    public BoardState getState() {
        return state;
    }

    public String getLocation() {
        return location;
    }

    public Region getRegion() {
        return region;
    }

    public Integer getContractDeposit() {
        return contractDeposit;
    }

    public Integer getContractMonthlyFee() {
        return contractMonthlyFee;
    }

    public void setCompleteData(CompleteDataDTO completeData) {
        if (completeData.getPrice() != null) {
            this.price = Integer.valueOf(completeData.getPrice());
        }
        if (completeData.getContractFee() != null) {
            this.contractMonthlyFee = Integer.valueOf(completeData.getContractFee());
        }
        if (completeData.getContractDeposit() != null) {
            this.contractDeposit = Integer.valueOf(completeData.getContractDeposit());
        }
    }

    public void setState(BoardState state) {
        this.state = state;
    }

    public void setNewBoard(Map<String, String> data) {
        this.state = BoardState.OPEN;
        hitCount = 0;
        this.cordX = "0";
        this.cordY = "0";

        if (data.containsKey("road_address")) {
            this.cordX = data.get("road_address_x");
            this.cordY = data.get("road_address_y");
        } else if (data.containsKey("address")) {
            this.cordX = data.get("address_x");
            this.cordY = data.get("address_y");
        }

        LocalDateTime now = LocalDateTime.now();
        setCreatedAt(now);
        setUpdatedAt(now);
    }

    public void setCoordinate(String cordX, String cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public String getCordX() {
        return cordX;
    }

    public String getCordY() {
        return cordY;
    }

    public static RoomBoard createEntityFromDTO(BoardDetailDTO boardDetailDTO) {

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
                .price(boardDetailDTO.getPrice())
                .priceType(boardDetailDTO.getPriceType())
                .deposit(boardDetailDTO.getDeposit())
                .fixedOption(boardDetailDTO.getFixedOption())
                .additionalOption(boardDetailDTO.getAdditionalOption())
                .details(boardDetailDTO.getDetails())
                .build();

        if (boardDetailDTO.getFiles() != null) {
            List<BoardFiles> boardFiles = roomBoard.getBoardFiles();
            for (FilesDTO filesDTO : boardDetailDTO.getFiles()) {
                boardFiles.add(BoardFiles.makeRelation(roomBoard, Files.fromDTO(filesDTO)));
            }
        }

        return roomBoard;
    }

    public BoardDetailDTO toDetailDTO() {

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
                .price(this.price)
                .priceType(this.priceType)
                .deposit(this.deposit)
                .fixedOption(this.fixedOption)
                .additionalOption(this.additionalOption)
                .region(this.region.toRegionDetailDTO())
                .cordX(this.cordX)
                .cordY(this.cordY)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .removedAt(this.removedAt)
                .distance(this.distance)
                .details(this.details)
                .build();

        if (this.files != null) {
            List<FilesDTO> filesDTOs = new ArrayList<>();
            for (BoardFiles boardFiles : this.files) {
                filesDTOs.add(boardFiles.getFiles().toDTO());
            }
            boardDetailDTO.setFiles(filesDTOs);
        }

        return boardDetailDTO;
    }

    public BoardListDTO toListDTO() {
        return BoardListDTO.builder()
                .id(this.id)
                .title(this.title)
                .author(this.user.toDTO())
                .state(this.state)
                .hitCount(this.hitCount)
                .durationStart(this.durationStart)
                .durationEnd(this.durationEnd)
                .location(this.location)
                .locationDetail(this.locationDetail)
                .price(this.price)
                .priceType(this.priceType)
                .deposit(this.deposit)
                .cordX(this.cordX)
                .cordY(this.cordY)
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .removedAt(this.removedAt)
                .distance(this.distance)
                .region(this.region.toRegionDetailDTO())
                .thumbnail(this.files.isEmpty() ? null : this.files.get(0).getFiles().toDTO())
                .build();
    }

    public void setRegion(Region region) {
        if (region != null) {
            region.getRoomBoardList().remove(this);
        }
        this.region = region;
    }

    public void setDistance(BigDecimal x, BigDecimal y) {

        BigDecimal xDiff = x.subtract(new BigDecimal(this.cordX));
        BigDecimal yDiff = y.subtract(new BigDecimal(this.cordY));
        this.distance = xDiff.pow(2).add(yDiff.pow(2));
    }

    public BigDecimal getDistance() {
        return this.distance;
    }
}
