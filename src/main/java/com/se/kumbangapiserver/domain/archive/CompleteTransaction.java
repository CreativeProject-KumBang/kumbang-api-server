package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "complete_transaction")
public class CompleteTransaction extends BaseTimeEntity {

    @Id
    @Column(name = "complete_transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tran_address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "close_year")
    private String year;

    @Column(name = "close_month")
    private String month;

    @Column(name = "close_day")
    private String day;

    @Column(name = "price")
    private String price;

    @Column(name = "contract_deposit")
    private String contractDeposit;

    @Column(name = "contract_fee")
    private String contractFee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private RoomBoard roomBoard;

}
