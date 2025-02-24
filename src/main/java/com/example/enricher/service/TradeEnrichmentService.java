package com.example.enricher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.CompletableFuture;

@Service
public class TradeEnrichmentService {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(TradeEnrichmentService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public TradeEnrichmentService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Асинхронная версия метода обогащения CSV.
     */
    @Async
    public CompletableFuture<String> enrichTradesAsync(String csvData) {
        StringBuilder sb = new StringBuilder();
        sb.append("date,productName,currency,price\n");

        try (BufferedReader reader = new BufferedReader(new StringReader(csvData))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false; // пропускаем заголовок входящего CSV
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length < 4) {
                    logger.error("Invalid row format: {}", line);
                    continue;
                }
                String dateStr = tokens[0].trim();
                String productId = tokens[1].trim();
                String currency = tokens[2].trim();
                String price = tokens[3].trim();

                // Валидация даты
                try {
                    LocalDate.parse(dateStr, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    logger.error("Invalid date format for row: {}", line);
                    continue;
                }

                // Получение названия продукта
                String productName = productService.getProductName(productId);

                sb.append(dateStr).append(",")
                        .append(productName).append(",")
                        .append(currency).append(",")
                        .append(price).append("\n");
            }
        } catch (IOException e) {
            logger.error("Error processing CSV data", e);
        }

        // Оборачиваем результат в CompletableFuture
        return CompletableFuture.completedFuture(sb.toString());
    }
}
