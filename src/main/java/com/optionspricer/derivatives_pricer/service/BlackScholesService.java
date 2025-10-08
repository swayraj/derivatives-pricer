package com.optionspricer.derivatives_pricer.service;

import com.optionspricer.derivatives_pricer.domain.MarketData;
import com.optionspricer.derivatives_pricer.domain.Option;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class BlackScholesService {

    //Method that implements core business logic behind black-scholes
    public double calculateOptionPrice(Option option, MarketData marketData)
    {
        //get the parameters required for the calculation
        double S = marketData.getStockPrice();
        double K = option.getStrikePrice();
        double r = marketData.getRiskFreeRate();
        double sigma = marketData.getVolatility();

        //calculating the expiry time in years
        long daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), option.getExpiryDate());
        double T = daysToExpiry/365.0;

        //calculate functions d1 and d2
        double d1 = (Math.log(S / K) + (r + (sigma * sigma) / 2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        //Using Cumulative Normal Distribution to find N(d1) and N(d2)
        NormalDistribution nd = new NormalDistribution();
        double Nd1 = nd.cumulativeProbability(d1);
        double Nd2 = nd.cumulativeProbability(d2);

        //Calculating the final option price based on type(i.e is Call or Put)
        double callPrice = S * Nd1 - K * Math.exp(-r * T) * Nd2;
        double putPrice = K * Math.exp(-r * T) * (1 - Nd2) - S * (1 - Nd1);

        if ("CALL".equalsIgnoreCase(option.getOptionType())) {
            return callPrice;
        } else if ("PUT".equalsIgnoreCase(option.getOptionType())) {
            return putPrice;
        } else {
            // Handle invalid option type, perhaps by throwing an exception
            return 0.0;
        }

    }

}
