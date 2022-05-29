package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.dto.RegionDetailDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "state") // 시 도
    private String state;

    @Column(name = "city") // 시군구
    private String city;

    @Column(name = "town") // 읍면동
    private String town;
    @Column(name = "total_entx", scale = 10, precision = 14) // 경도
    private BigDecimal entx;

    @Column(name = "total_enty", scale = 10, precision = 14) // 위도
    private BigDecimal enty;

    @Column(name = "avg_entx", scale = 10, precision = 14) // 평균경도
    private BigDecimal avgEntx;

    @Column(name = "avg_enty", scale = 10, precision = 14) // 평균위도
    private BigDecimal avgEnty;

    @Column(name = "quantity") // 매물 수량
    private Integer quantity;

    @Column(name = "open_board_total_price", precision = 20, scale = 2) // 매물 총가격
    private BigDecimal totalPrice;

    @Column(name = "open_board_avg_price", precision = 20, scale = 2) // 매물 평균가격
    private BigDecimal avgPrice;

    @OneToMany(mappedBy = "region", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<RoomBoard> roomBoardList;

    public List<RoomBoard> getRoomBoardList() {
        return roomBoardList;
    }

    public void addBoard(RoomBoard roomBoard) {
        BigDecimal x = this.entx;
        BigDecimal y = this.enty;
        BigDecimal total = this.totalPrice;
        BigDecimal quantity = new BigDecimal(this.quantity);

        x = x.add(new BigDecimal(roomBoard.getCordX()));
        y = y.add(new BigDecimal(roomBoard.getCordY()));
        total = total.add(new BigDecimal(roomBoard.getPrice()));
        quantity = quantity.add(BigDecimal.ONE);

        this.avgEntx = x.divide(quantity, 10, RoundingMode.HALF_UP);
        this.avgEnty = y.divide(quantity, 10, RoundingMode.HALF_UP);
        this.avgPrice = total.divide(quantity, 2, RoundingMode.HALF_UP);

        this.entx = x.setScale(10, RoundingMode.HALF_UP);
        this.enty = y.setScale(10, RoundingMode.HALF_UP);
        this.totalPrice = total.setScale(2, RoundingMode.HALF_UP);
        this.quantity = quantity.intValue();
    }

    public void removeBoard(RoomBoard roomBoard) {
        BigDecimal x = this.entx;
        BigDecimal y = this.enty;
        BigDecimal total = this.totalPrice;
        BigDecimal quantity = new BigDecimal(this.quantity);

        x = x.subtract(new BigDecimal(roomBoard.getCordX()));
        y = y.subtract(new BigDecimal(roomBoard.getCordY()));
        total = total.subtract(new BigDecimal(roomBoard.getPrice()));
        quantity = quantity.subtract(BigDecimal.ONE);

        if (quantity.compareTo(BigDecimal.ZERO) != 0) {
            this.avgEntx = x.divide(quantity, 10, RoundingMode.HALF_UP);
            this.avgEnty = y.divide(quantity, 10, RoundingMode.HALF_UP);
            this.avgPrice = total.divide(quantity, 2, RoundingMode.HALF_UP);
        } else {
            this.avgEntx = BigDecimal.ZERO;
            this.avgEnty = BigDecimal.ZERO;
            this.avgPrice = BigDecimal.ZERO;
        }
        this.entx = x.setScale(10, RoundingMode.HALF_UP);
        this.enty = y.setScale(10, RoundingMode.HALF_UP);
        this.totalPrice = total.setScale(2, RoundingMode.HALF_UP);
        this.quantity = quantity.intValue();
    }

    public static Region fromRegionDetailDTO(RegionDetailDTO region) {
        return Region.builder()
                .state(region.getState())
                .city(region.getCity())
                .town(region.getTown())
                .enty(region.getEnty())
                .entx(region.getEntx())
                .quantity(Integer.valueOf(region.getQuantity()))
                .build();
    }

    public RegionDetailDTO toRegionDetailDTO() {
        return RegionDetailDTO.builder()
                .id(this.id)
                .state(this.state)
                .city(this.city)
                .town(this.town)
                .enty(this.avgEnty)
                .entx(this.avgEntx)
                .quantity(this.quantity.toString())
                .price(this.avgPrice)
                .build();
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }
}
