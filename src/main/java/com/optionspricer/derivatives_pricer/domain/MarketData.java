package com.optionspricer.derivatives_pricer.domain;

import lombok.Data;

@Data
public class MarketData {

    //current stock price
    private double stockPrice;

    //Risk-free interest rate or dividend
    private double riskFreeRate;

    //Higher Volatility of Stock Price means greater Option Value
    //Predicted by the python service
    private double volatility;

}
