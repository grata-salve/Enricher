package com.example.enricher.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PRODUCT_KEY_PREFIX = "product:";
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @PostConstruct
    public void loadProducts() {
        ClassPathResource resource = new ClassPathResource("product.csv");
        try (InputStream is = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length >= 2) {
                    String productId = tokens[0].trim();
                    String productName = tokens[1].trim();
                    redisTemplate.opsForValue().set(PRODUCT_KEY_PREFIX + productId, productName);
                }
            }
            logger.info("Product data loaded successfully into Redis.");
        } catch (IOException e) {
            logger.error("Error loading product data", e);
        }
    }

    public String getProductName(String productId) {
        String productName = redisTemplate.opsForValue().get(PRODUCT_KEY_PREFIX + productId);
        if (productName == null) {
            //logger.error("Missing mapping for productId: {}", productId);
            return "Missing Product Name";
        }
        return productName;
    }
}
