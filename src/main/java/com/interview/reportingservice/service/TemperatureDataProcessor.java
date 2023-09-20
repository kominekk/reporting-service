package com.interview.reportingservice.service;

import com.interview.reportingservice.configuration.DataProcessorConfig;
import com.interview.reportingservice.model.TemperatureMeasurement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Component
@Slf4j
public class TemperatureDataProcessor {
    private final TemperatureDataStorage temperatureDataStorage;
    private final Path dataFilePath;
    private long numberOfProcessedLines = 0;
    private final DateTimeFormatter formatter;
    private static final String DELIMITER = ";";


    public TemperatureDataProcessor(DataProcessorConfig dataProcessorConfig, TemperatureDataStorage temperatureDataStorage) {
        this.temperatureDataStorage = temperatureDataStorage;
        this.dataFilePath = dataProcessorConfig.getFilePath();
        this.formatter = dataProcessorConfig.getFormatter();
    }

    void processNewData() {
        log.info("[Process New Data] START");
        try (BufferedReader bufferedReader = Files.newBufferedReader(dataFilePath)) {
            Stream<String> linesStream = bufferedReader.lines();
            linesStream.skip(numberOfProcessedLines).forEach(line -> {
                processLine(line);
                numberOfProcessedLines = numberOfProcessedLines + 1;
            });
            log.info("[Process New Data] END");
        } catch (IOException exception) {
            log.error("read new data failed, reason={}", exception.getMessage());
        }
    }

    private void processLine(String line){
        try {
            String[] splitCsvLine = line.split(DELIMITER);
            String city = splitCsvLine[0];
            LocalDateTime measurementDate = LocalDateTime.parse(splitCsvLine[1], formatter);
            BigDecimal temperature = new BigDecimal(splitCsvLine[2]);
            TemperatureMeasurement temperatureMeasurement = TemperatureMeasurement.builder()
                    .year(measurementDate.getYear())
                    .city(city)
                    .temperature(temperature)
                    .build();
            temperatureDataStorage.addMeasurement(temperatureMeasurement);
        } catch (Exception exception) {
            log.error("Could not parse line={}, reason={}", line, exception.getMessage());
        }
    }
}
