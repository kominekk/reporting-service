package com.interview.reportingservice.controller;

import com.interview.reportingservice.model.YearlyAverageTemperature;
import com.interview.reportingservice.service.TemperatureReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {
    private final TemperatureReportService temperatureReportService;

    @GetMapping("/temperature/yearly-average")
    public ResponseEntity<List<YearlyAverageTemperature>> getYearlyTemperatureReportForProvidedCity(@RequestParam String city){
        return ResponseEntity.ok(temperatureReportService.getYearlyTemperatureReportForProvidedCity(city));
    }
}
