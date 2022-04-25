package com.se.kumbangapiserver.domain.board;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Builder
@Getter

@NoArgsConstructor
@AllArgsConstructor

@ToString

@Embeddable
public class Details {
    @Column(name = "parking")
    private String parking;

    @Column(name = "elevator")
    private String elevator;

    @Column(name = "room_structure")
    private String roomStructure;

    @Column(name = "management_fee")
    private String managementFee;

    @Column(name = "area_size")
    private String areaSize;

    @Column(name = "contain_manage_fee")
    private String containManageFee;

}
