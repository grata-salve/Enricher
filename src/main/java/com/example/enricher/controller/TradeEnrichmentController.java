package com.example.enricher.controller;

import com.example.enricher.service.TradeEnrichmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1")
public class TradeEnrichmentController {

    private final TradeEnrichmentService tradeEnrichmentService;

    public TradeEnrichmentController(TradeEnrichmentService tradeEnrichmentService) {
        this.tradeEnrichmentService = tradeEnrichmentService;
    }

    @PostMapping(value = "/enrich",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "text/csv")
    public CompletableFuture<ResponseEntity<String>> enrichTradesAsync(@RequestParam("file") MultipartFile file) {
        try {
            String csvData = new String(file.getBytes(), StandardCharsets.UTF_8);
            // Асинхронный вызов enrichTradesAsync
            return tradeEnrichmentService.enrichTradesAsync(csvData)
                    .thenApply(enrichedCsv -> ResponseEntity.ok(enrichedCsv));
        } catch (IOException e) {
            return CompletableFuture.completedFuture(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file")
            );
        }
    }
}
