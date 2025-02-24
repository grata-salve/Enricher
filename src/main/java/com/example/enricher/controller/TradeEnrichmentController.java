package com.example.enricher.controller;

import com.example.enricher.service.TradeEnrichmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TradeEnrichmentController {

    private final TradeEnrichmentService tradeEnrichmentService;

    public TradeEnrichmentController(TradeEnrichmentService tradeEnrichmentService) {
        this.tradeEnrichmentService = tradeEnrichmentService;
    }

    @PostMapping(value = "/enrich", consumes = "text/csv", produces = "text/csv")
    public ResponseEntity<String> enrichTrades(@RequestBody String csvData) {
        String enrichedCsv = tradeEnrichmentService.enrichTrades(csvData);
        return ResponseEntity.ok(enrichedCsv);
    }
}
