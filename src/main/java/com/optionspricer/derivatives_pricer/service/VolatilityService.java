package com.optionspricer.derivatives_pricer.service;

import com.optionspricer.derivatives_pricer.domain.Option;
import com.optionspricer.derivatives_pricer.dto.VolatilityRequest;
import com.optionspricer.derivatives_pricer.dto.VolatilityResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;

@Service
public class VolatilityService {

    private final RestTemplate restTemplate = new RestTemplate();

    //Injected from app.props
    @Value("${volatility.service.url}")
    private String pythonApiUrl;

    //Setting up request to Volatility Request
    public double getPredictedVolatility(Option option)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        VolatilityRequest request = new VolatilityRequest(
                option.getStrikePrice(),
                option.getExpiryDate().format(formatter),
                option.getOptionType());

        //Receiving response from VolatilityResponse
        VolatilityResponse response = restTemplate.postForObject(pythonApiUrl, request, VolatilityResponse.class);

        if (response != null) {
            return response.getPredicted_volatility();
        } else {
            throw new RuntimeException("Failed to get volatility prediction from Python service. Response was null.");
        }

    }


}
