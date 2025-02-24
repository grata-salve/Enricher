package com.example.enricher.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductServiceTest {

    @Test
    public void testGetProductName() {
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("product:1")).thenReturn("Treasury Bills Domestic");

        ProductService productService = new ProductService(redisTemplate);
        String productName = productService.getProductName("1");
        assertEquals("Treasury Bills Domestic", productName);
    }

    @Test
    public void testGetMissingProductName() {
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        when(valueOps.get("product:99")).thenReturn(null);

        ProductService productService = new ProductService(redisTemplate);
        String productName = productService.getProductName("99");
        assertEquals("Missing Product Name", productName);
    }
}
