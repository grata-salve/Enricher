package com.example.enricher.service;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TradeEnrichmentServiceTest {

    @Test
    public void testEnrichTrades() {
        // Создаем mock для ProductService
        ProductService productService = mock(ProductService.class);
        when(productService.getProductName("1")).thenReturn("Treasury Bills Domestic");
        when(productService.getProductName("2")).thenReturn("Corporate Bonds Domestic");
        when(productService.getProductName("3")).thenReturn("REPO Domestic");
        // Для productId, для которого нет маппинга, возвращаем "Missing Product Name"
        when(productService.getProductName("11")).thenReturn("Missing Product Name");

        String inputCsv = "date,productId,currency,price\n" +
                "20230101,1,USD,100.25\n" +
                "20230101,2,EUR,200.45\n" +
                "20230101,3,GBP,300.50\n" +
                "20230105,11,EUR,600.50\n" +
                "invalidDate,1,EUR,1700.70\n";

        TradeEnrichmentService service = new TradeEnrichmentService(productService);
        String resultCsv = service.enrichTrades(inputCsv);

        String expected = "date,productName,currency,price\n" +
                "20230101,Treasury Bills Domestic,USD,100.25\n" +
                "20230101,Corporate Bonds Domestic,EUR,200.45\n" +
                "20230101,REPO Domestic,GBP,300.50\n" +
                "20230105,Missing Product Name,EUR,600.50\n";

        assertEquals(expected, resultCsv);
    }
}
