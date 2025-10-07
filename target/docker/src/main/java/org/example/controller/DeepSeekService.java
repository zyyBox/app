package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

@Service
public class DeepSeekService {

    private final String apiKey = "sk-629e0af2be2443a89f87458eef2af2ba";
    private final String apiUrl = "https://api.deepseek.com/chat/completions";
    @Autowired
    private RestTemplate restTemplate;
    public String callDeepSeekApi(String input) {

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",StandardCharsets.UTF_8));
        headers.set("Authorization", "Bearer " + apiKey);

        // 创建请求体
        String requestBody = "{\"model\": \"deepseek-reasoner\", \"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}], \"max_tokens\": 8192}";
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // 发送请求
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            // 返回响应
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String reply = rootNode.path("choices").get(0).path("message").path("content").asText();

            return reply;
        }catch (HttpClientErrorException e){
            return "调用失败"+e.getResponseBodyAsString();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
