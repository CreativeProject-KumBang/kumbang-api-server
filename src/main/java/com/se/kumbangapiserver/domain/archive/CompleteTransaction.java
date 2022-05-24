package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_board_id")
    private RoomBoard roomBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

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

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "price")
    private String price;

    @Column(name = "contract_deposit")
    private String contractDeposit;

    @Column(name = "contract_fee")
    private String contractFee;


    public TransactionDataDTO toDTO() {
        return TransactionDataDTO.builder()
                .id(id)
                .address(address)
                .region(region.toRegionDetailDTO())
                .year(year)
                .board(roomBoard.toDetailDTO())
                .buyer(buyer.toDTO())
                .month(month)
                .day(day)
                .price(price)
                .contractDeposit(contractDeposit)
                .contractFee(contractFee)
                .build();
    }
}
