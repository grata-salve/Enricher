package com.example.enricher.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductServiceTest {

    @Test
    public void testGetProductName() {
        // Создаем мок RedisTemplate
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        // Создаем мок ValueOperations
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        // Настраиваем redisTemplate, чтобы opsForValue() возвращал наш мок
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        // Настраиваем поведение для существующего продукта
        when(valueOps.get("product:1")).thenReturn("Treasury Bills Domestic");

        ProductService productService = new ProductService(redisTemplate);
        String productName = productService.getProductName("1");
        assertEquals("Treasury Bills Domestic", productName);
    }

    @Test
    public void testGetMissingProductName() {
        // Создаем мок RedisTemplate
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        // Создаем мок ValueOperations
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        // Настраиваем redisTemplate, чтобы opsForValue() возвращал наш мок
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
        // Настраиваем поведение для отсутствующего продукта
        when(valueOps.get("product:99")).thenReturn(null);

        ProductService productService = new ProductService(redisTemplate);
        String productName = productService.getProductName("99");
        assertEquals("Missing Product Name", productName);
    }
}
