// src/main/java/org/example/controller/DeepSeekService.java
package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Service
public class DeepSeekService {

    @Value("${deepseek.api.url:https://api.deepseek.com/chat/completions}")
    private String apiUrl;

    private final String apiKey = "sk-629e0af2be2443a89f87458eef2af2ba";

    @Autowired
    private RestTemplate restTemplate;

    public String callDeepSeekApi(String input) {
        // 输入验证
        if (input == null) {
            input = "";
        }

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Authorization", "Bearer " + apiKey);

        // 转义输入中的特殊字符
        String escapedInput = input.replace("\"", "\\\"").replace("\n", "\\n");
        String requestBody = "{\"model\": \"deepseek-reasoner\", \"messages\": [{\"role\": \"user\", \"content\": \"" + escapedInput + "\"}], \"max_tokens\": 8192}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // 发送请求
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

            // 检查响应
            if (response.getBody() == null) {
                return "API返回空响应";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.getBody());

            // 验证JSON结构
            if (!rootNode.has("choices") || rootNode.get("choices").size() == 0) {
                return "API响应格式不正确: " + response.getBody();
            }

            String reply = rootNode.path("choices").get(0).path("message").path("content").asText();
            return reply;

        } catch (HttpClientErrorException e) {
            // 修复：确保状态码被正确包含在错误消息中
            String responseBody = e.getResponseBodyAsString();
            String errorMessage = "调用失败: " + e.getStatusCode().value(); // 使用 .value() 获取数字状态码

            if (responseBody != null && !responseBody.trim().isEmpty()) {
                errorMessage += " - " + responseBody;
            } else {
                errorMessage += " - " + e.getStatusText(); // 如果没有响应体，至少包含状态文本
            }

            return errorMessage;
        } catch (JsonProcessingException e) {
            return "JSON解析失败: " + e.getMessage();
        } catch (IOException e) {
            return "IO错误: " + e.getMessage();
        } catch (Exception e) {
            return "未知错误: " + e.getMessage();
        }
    }

    // 添加setter方法用于测试
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}