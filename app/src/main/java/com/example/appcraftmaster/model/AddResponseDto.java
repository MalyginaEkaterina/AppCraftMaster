package com.example.appcraftmaster.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddResponseDto {
    private Long offerId;
    private BigDecimal price;
    private Long dateBeg;
    private Long dateEnd;
}
