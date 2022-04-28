package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.dto.RegionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "enty") // 위도
    private String enty;

    @Column(name = "entx") // 경도
    private String entx;

    @Column(name = "quantity") // 매물 수량
    private Integer quantity;

    @OneToMany(mappedBy = "region")
    private List<RoomBoard> roomBoardList;

    public List<RoomBoard> getRoomBoardList() {
        return roomBoardList;
    }

    public static Region fromDTO(RegionDTO region) {
        return Region.builder()
                .state(region.getState())
                .city(region.getCity())
                .town(region.getTown())
                .enty(region.getEnty())
                .entx(region.getEntx())
                .quantity(Integer.valueOf(region.getQuantity()))
                .build();
    }
}
