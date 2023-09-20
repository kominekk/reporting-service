package com.interview.reportingservice.service;

import com.interview.reportingservice.configuration.DataProcessorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;

@Component
@Slf4j
public class TemperatureDataChangeListener {
    private final TemperatureDataProcessor temperatureDataProcessor;
    private final Path dataFilePath;

    public TemperatureDataChangeListener(DataProcessorConfig dataProcessorConfig, TemperatureDataProcessor temperatureDataProcessor) {
        this.temperatureDataProcessor = temperatureDataProcessor;
        this.dataFilePath = dataProcessorConfig.getFilePath();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        temperatureDataProcessor.processNewData();
        checkForFileChanges();
    }

    public void checkForFileChanges() {
        log.info("[Check for new data] START");
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            dataFilePath.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey key;
            while ((key = watchService.take()) != null) {
                temperatureDataProcessor.processNewData();
                key.reset();
            }
            log.info("[Check for new data] END");
        } catch (IOException | InterruptedException exception) {
            log.error(exception.getMessage());
        }
    }
}
