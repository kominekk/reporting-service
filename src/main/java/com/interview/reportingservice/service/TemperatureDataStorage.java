package com.interview.reportingservice.service;

import com.interview.reportingservice.model.TemperatureMeasurement;
import com.interview.reportingservice.model.TemperatureStatistic;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TemperatureDataStorage {

    private final static ConcurrentHashMap<String, ConcurrentHashMap<Integer, TemperatureStatistic>> TEMPERATURE_STATISTIC_MAP = new ConcurrentHashMap<>();

    void addMeasurement(TemperatureMeasurement temperatureMeasurement) {
        String city = temperatureMeasurement.getCity().toLowerCase();
        int year = temperatureMeasurement.getYear();

        TEMPERATURE_STATISTIC_MAP.putIfAbsent(city, new ConcurrentHashMap<>());
        TEMPERATURE_STATISTIC_MAP.get(city).compute(year, (k, statistic) -> {
                    if (statistic == null) {
                        return TemperatureStatistic.builder()
                                .measurementQty(1)
                                .temperatureAmount(temperatureMeasurement.getTemperature())
                                .build();
                    } else {
                        statistic.addMeasurement(temperatureMeasurement.getTemperature());
                        return statistic;
                    }
                });
    }

    public Map<Integer, TemperatureStatistic> getStatisticByCity(String city) {
        if (Strings.isNotBlank(city) && TEMPERATURE_STATISTIC_MAP.containsKey(city.toLowerCase())) {
             return Collections.unmodifiableMap(TEMPERATURE_STATISTIC_MAP.get(city));
        }
        return Map.of();
    }
}
