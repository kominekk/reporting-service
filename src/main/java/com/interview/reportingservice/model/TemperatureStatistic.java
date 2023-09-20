package com.interview.reportingservice.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class TemperatureStatistic {
    long measurementQty;
    BigDecimal temperatureAmount;

    public void addMeasurement(BigDecimal temperature) {
        measurementQty++ ;
        temperatureAmount = temperatureAmount.add(temperature);
    }
}
