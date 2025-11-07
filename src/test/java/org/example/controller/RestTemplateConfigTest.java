// src/test/java/org/example/controller/RestTemplateConfigTest.java
package org.example.controller;

import org.junit.Test;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class RestTemplateConfigTest {

    private RestTemplateConfig restTemplateConfig = new RestTemplateConfig();

    @Test
    public void testRestTemplate_BeanConfiguration() {
        // 执行测试
        RestTemplate restTemplate = restTemplateConfig.restTemplate();

        // 验证结果
        assertNotNull(restTemplate);

        // 验证StringHttpMessageConverter字符编码
        boolean foundStringConverter = false;
        for (Object converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter stringConverter = (StringHttpMessageConverter) converter;
                assertEquals(StandardCharsets.UTF_8, stringConverter.getDefaultCharset());
                foundStringConverter = true;
            }
        }
        assertTrue("应该找到 StringHttpMessageConverter", foundStringConverter);
    }

    @Test
    public void testRestTemplate_NotNull() {
        RestTemplate restTemplate = restTemplateConfig.restTemplate();
        assertNotNull("RestTemplate 不应该为 null", restTemplate);
    }
}