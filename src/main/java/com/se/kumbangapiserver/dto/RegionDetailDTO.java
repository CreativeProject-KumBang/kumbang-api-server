package com.se.kumbangapiserver.dto;

import com.se.kumbangapiserver.domain.archive.Region;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder

@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
public class RegionDetailDTO {
    private Long id;
    private String state;
    private String city;
    private String town;
    private BigDecimal entx;
    private BigDecimal enty;
    private String quantity;
    private BigDecimal price;

    public RegionDetailDTO(BigDecimal entx, BigDecimal enty, Long quantity, String state, String city, BigDecimal totalPrice) {
        this.entx = entx;
        this.enty = enty;
        this.quantity = quantity.toString();
        this.state = state;
        this.city = city;
        this.price = totalPrice;
    }

}
