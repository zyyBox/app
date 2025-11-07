// src/test/java/org/example/controller/DeepSeekControllerTest.java
package org.example.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeepSeekControllerTest {

    @Mock
    private DeepSeekService deepSeekService;

    @InjectMocks
    private DeepSeekController deepSeekController;

    @Test
    public void testCallDeepSeek_ValidInput() {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", "Hello, DeepSeek");

        String expectedResponse = "DeepSeek的回复内容";

        // 模拟Service行为
        when(deepSeekService.callDeepSeekApi("Hello, DeepSeek")).thenReturn(expectedResponse);

        // 执行测试
        String result = deepSeekController.callDeepSeek(request);

        // 验证结果
        assertEquals(expectedResponse, result);
        verify(deepSeekService).callDeepSeekApi("Hello, DeepSeek");
    }

    @Test
    public void testCallDeepSeek_EmptyInput() {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", "");
        String expectedResponse = "Empty response";

        when(deepSeekService.callDeepSeekApi("")).thenReturn(expectedResponse);

        // 执行测试
        String result = deepSeekController.callDeepSeek(request);

        // 验证结果
        assertEquals(expectedResponse, result);
    }

    @Test
    public void testCallDeepSeek_NullInput() {
        // 准备测试数据
        Map<String, String> request = new HashMap<>();
        request.put("input", null);
        String expectedResponse = "Null input response";

        when(deepSeekService.callDeepSeekApi(null)).thenReturn(expectedResponse);

        // 执行测试
        String result = deepSeekController.callDeepSeek(request);

        // 验证结果
        assertEquals(expectedResponse, result);
    }

    @Test
    public void testCallDeepSeek_MissingInputField() {
        // 准备测试数据 - 没有 input 字段
        Map<String, String> request = new HashMap<>();
        String expectedResponse = "No input provided";

        when(deepSeekService.callDeepSeekApi(null)).thenReturn(expectedResponse);

        // 执行测试
        String result = deepSeekController.callDeepSeek(request);

        // 验证结果
        assertEquals(expectedResponse, result);
    }
}