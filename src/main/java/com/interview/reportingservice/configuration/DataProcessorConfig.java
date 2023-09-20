package com.interview.reportingservice.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@Getter
@Setter
@ConfigurationProperties("dataprocessor")
public class DataProcessorConfig {

    String dataFilePath;
    String dateFormat;

    public Path getFilePath() {
        try {
            return Paths.get(ResourceUtils.getFile(dataFilePath).toURI());
        } catch (FileNotFoundException exception) {
            log.error("Could not find file={}, message={}", dataFilePath, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(dateFormat);
    }
}
