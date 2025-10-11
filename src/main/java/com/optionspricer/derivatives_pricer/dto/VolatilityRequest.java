package com.optionspricer.derivatives_pricer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolatilityRequest {

    private double strike_price;
    private String expirationDate;
    private String type;

}
