package com.example.enricher.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEnrichEndpointWithFile() throws Exception {
        String csvData = "date,productId,currency,price\n" +
                "20230101,1,USD,100.25\n" +
                "20230101,2,EUR,200.45\n" +
                "invalidDate,1,EUR,1700.70\n";

        MockMultipartFile file = new MockMultipartFile("file", "trade.csv", "text/csv", csvData.getBytes());

        mockMvc.perform(multipart("/api/v1/enrich").file(file))
                .andExpect(status().isOk());
    }
}
