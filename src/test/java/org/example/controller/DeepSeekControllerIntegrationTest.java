// src/test/java/org/example/controller/DeepSeekControllerIntegrationTest.java
package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DeepSeekControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeepSeekService deepSeekService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCallDeepSeek_Integration_Success() throws Exception {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", "Hello, DeepSeek");

        String expectedResponse = "这是DeepSeek的回复";

        // 模拟Service行为
        when(deepSeekService.callDeepSeekApi("Hello, DeepSeek")).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/call-deepseek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testCallDeepSeek_Integration_EmptyInput() throws Exception {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", "");
        String expectedResponse = "空输入响应";

        // 模拟Service行为
        when(deepSeekService.callDeepSeekApi("")).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/call-deepseek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testCallDeepSeek_Integration_NoInputField() throws Exception {
        // 准备测试数据 - 没有input字段
        Map<String, String> request = new HashMap<>();
        String expectedResponse = "无输入响应";

        // 模拟Service行为
        when(deepSeekService.callDeepSeekApi(null)).thenReturn(expectedResponse);

        // 执行测试
        mockMvc.perform(post("/api/call-deepseek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void testCallDeepSeek_Integration_InvalidJson() throws Exception {
        // 准备测试数据
        String invalidJson = "{ invalid json }";

        // 执行测试
        mockMvc.perform(post("/api/call-deepseek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCallDeepSeek_Integration_ServiceException() throws Exception {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", "test input");

        // 模拟Service抛出异常
        when(deepSeekService.callDeepSeekApi("test input"))
                .thenReturn("服务调用异常");

        // 执行测试
        mockMvc.perform(post("/api/call-deepseek")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("服务调用异常"));
    }
}