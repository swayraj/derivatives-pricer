package com.optionspricer.derivatives_pricer.repository;

import com.optionspricer.derivatives_pricer.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {

    //Wrapped and methods are now available
}
