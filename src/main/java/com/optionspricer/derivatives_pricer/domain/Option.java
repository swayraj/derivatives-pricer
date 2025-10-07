package com.optionspricer.derivatives_pricer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Call or Put Option
    private String optionType;

    //price at which asset can be bought or sold
    private double strikePrice;

    //Option or Contract Expiry Date
    private LocalDate expiryDate;

}
