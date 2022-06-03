package com.se.kumbangapiserver.domain.archive;

import com.se.kumbangapiserver.domain.board.RoomBoard;
import com.se.kumbangapiserver.domain.common.BaseTimeEntity;
import com.se.kumbangapiserver.domain.user.User;
import com.se.kumbangapiserver.dto.CompleteDataDTO;
import com.se.kumbangapiserver.dto.TransactionDataDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Table(name = "complete_transaction")
public class CompleteTransaction extends BaseTimeEntity {

    @Id
    @Column(name = "complete_transaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "room_board_id")
    private RoomBoard roomBoard;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
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

    @Column(name = "deposit")
    private String deposit;

    @Column(name = "contract_deposit")
    private String contractDeposit;

    @Column(name = "contract_fee")
    private String contractFee;


    public TransactionDataDTO toTransactionDTO() {
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

    public CompleteDataDTO toCompleteDTO() {
        return CompleteDataDTO.builder()
                .id(String.valueOf(id))
                .contractFee(contractFee)
                .contractDeposit(contractDeposit)
                .price(price)
                .deposit(deposit)
                .startDate(startDate)
                .endDate(endDate)
                .address(address)
                .build();
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }
}
