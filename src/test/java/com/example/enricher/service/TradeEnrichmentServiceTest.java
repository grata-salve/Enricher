package com.example.enricher.service;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TradeEnrichmentServiceTest {

    @Test
    public void testEnrichTrades() {
        ProductService productService = mock(ProductService.class);
        when(productService.getProductName("1")).thenReturn("Treasury Bills Domestic");
        when(productService.getProductName("2")).thenReturn("Corporate Bonds Domestic");
        when(productService.getProductName("3")).thenReturn("REPO Domestic");
        when(productService.getProductName("11")).thenReturn("Missing Product Name");

        String inputCsv = "date,productId,currency,price\n" +
                "20230101,1,USD,100.25\n" +
                "20230101,2,EUR,200.45\n" +
                "20230101,3,GBP,300.50\n" +
                "20230105,11,EUR,600.50\n" +
                "invalidDate,1,EUR,1700.70";

        InputStream inputStream = new ByteArrayInputStream(inputCsv.getBytes(StandardCharsets.UTF_8));

        TradeEnrichmentService service = new TradeEnrichmentService(productService);
        CompletableFuture<String> futureResult = service.enrichTrades(inputStream);
        String resultCsv = futureResult.join();

        String expected = "date,productName,currency,price\n" +
                "20230101,Treasury Bills Domestic,USD,100.25\n" +
                "20230101,Corporate Bonds Domestic,EUR,200.45\n" +
                "20230101,REPO Domestic,GBP,300.50\n" +
                "20230105,Missing Product Name,EUR,600.50";

        assertEquals(expected, resultCsv);
    }
}
