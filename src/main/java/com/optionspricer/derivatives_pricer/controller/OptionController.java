package com.optionspricer.derivatives_pricer.controller;

import com.optionspricer.derivatives_pricer.domain.MarketData;
import com.optionspricer.derivatives_pricer.domain.Option;
import com.optionspricer.derivatives_pricer.service.BlackScholesService;
import com.optionspricer.derivatives_pricer.service.VolatilityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class OptionController {

    private final BlackScholesService blackScholesService;
    private final VolatilityService volatilityService;

    //Dependency Injection
    public OptionController(BlackScholesService blackScholesService, VolatilityService volatilityService)
    {
        this.blackScholesService = blackScholesService;
        this.volatilityService = volatilityService;
    }

    //root url to html form
    @GetMapping("/")
    public String showPriceForm()
    {
        return "price-form";
    }

    //Mapping to handle form submission
    @PostMapping("/calculate")
    public String calculatePrice(
            @RequestParam double stockPrice,
            @RequestParam double strikePrice,
            @RequestParam("expiry_date") LocalDate expiryDate,
            @RequestParam String optionType,
            Model model)
    {
        //setting the values(@param) to Option Body
        Option option = new Option();
        option.setStrikePrice(strikePrice);
        option.setExpiryDate(expiryDate);
        option.setOptionType(optionType);

        //setting the values(@param) to MarketData Body
        MarketData marketData = new MarketData();
        marketData.setStockPrice(stockPrice);
        marketData.setRiskFreeRate(0.05); //Fixed Risk Free Rate for simplcity

        //calling the Python Model to predict the Volatility
        double predictedVolatility = volatilityService.getPredictedVolatility(option);
        marketData.setVolatility(predictedVolatility);

        //calculating the option price
        double optionPrice = blackScholesService.calculateOptionPrice(option, marketData);

        //results to the 'model' to be displayed on the results page
        model.addAttribute("optionPrice", optionPrice);
        model.addAttribute("predictedVolatility", predictedVolatility * 100); // For display as %
        model.addAttribute("optionInput", option); // Pass back the input for display
        model.addAttribute("marketInput", marketData);

        //display result form
        return "price-result";

    }



}
