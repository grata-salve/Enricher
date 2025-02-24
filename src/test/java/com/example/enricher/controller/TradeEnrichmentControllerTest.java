package com.example.enricher.controller;

import com.example.enricher.EnricherApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = EnricherApplication.class)
@AutoConfigureMockMvc
public class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEnrichEndpoint() throws Exception {
        String inputCsv = "date,productId,currency,price\n" +
                "20230101,1,USD,100.25\n" +
                "20230101,2,EUR,200.45\n" +
                "invalidDate,1,EUR,1700.70\n";

        // Обновленные ожидаемые данные в соответствии с вашим product.csv
        String expectedResponse = "date,productName,currency,price\n" +
                "20230101,Commodity Swaps 1,USD,100.25\n" +
                "20230101,Commodity Swaps,EUR,200.45\n";

        mockMvc.perform(post("/api/v1/enrich")
                        .contentType("text/csv")
                        .content(inputCsv))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }
}

