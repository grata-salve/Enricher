package com.example.enricher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class TradeEnrichmentService {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public TradeEnrichmentService(ProductService productService) {
        this.productService = productService;
    }


    /**
     * ## Task 3 + 4: Reactive Data Streaming + Async Processing
     *
     * Асинхронно обробляє CSV із використанням Java Stream API. Метод читає вхідний CSV порядково,
     * збагачує кожен рядок і збирає результат, не завантажуючи весь файл відразу у пам'ять.
     */
    @Async
    public CompletableFuture<String> enrichTrades(InputStream inputStream) {
        String header = "date,productName,currency,price\n";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String enrichedData = reader.lines()
                    .skip(1)
                    .map(this::processLine)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));
            return CompletableFuture.completedFuture(header + enrichedData);
        } catch (IOException e) {
            logger.error("Error processing CSV data", e);
            return CompletableFuture.completedFuture("");
        }
    }

    private String processLine(String line) {
        String[] tokens = line.split(",");
        if (tokens.length < 4) {
            logger.error("Invalid row format: {}", line);
            return null;
        }
        String dateStr = tokens[0].trim();
        try {
            LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            logger.error("Invalid date format for row: {}", line);
            return null;
        }
        String productId = tokens[1].trim();
        String currency = tokens[2].trim();
        String price = tokens[3].trim();
        String productName = productService.getProductName(productId);
        return dateStr + "," + productName + "," + currency + "," + price;
    }
}
