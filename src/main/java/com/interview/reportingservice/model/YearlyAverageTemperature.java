package com.interview.reportingservice.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class YearlyAverageTemperature {
    String year;
    BigDecimal averageTemperature;
}
