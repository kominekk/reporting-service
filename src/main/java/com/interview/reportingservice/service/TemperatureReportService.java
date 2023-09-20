package com.interview.reportingservice.service;

import com.interview.reportingservice.model.YearlyAverageTemperature;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TemperatureReportService {

    private final TemperatureDataStorage temperatureDataStorage;

    public TemperatureReportService(TemperatureDataStorage temperatureDataStorage) {
        this.temperatureDataStorage = temperatureDataStorage;
    }

    public List<YearlyAverageTemperature> getYearlyTemperatureReportForProvidedCity(String city) {
        if (Strings.isNotBlank(city)) {
            return temperatureDataStorage.getStatisticByCity(city)
                    .entrySet().stream()
                    .map(entry -> YearlyAverageTemperature.builder()
                            .year(entry.getKey().toString())
                            .averageTemperature(entry.getValue().getTemperatureAmount().divide(BigDecimal.valueOf(entry.getValue().getMeasurementQty()), 1, RoundingMode.HALF_DOWN))
                            .build())
                    .sorted((Comparator.comparing(YearlyAverageTemperature::getYear)))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
