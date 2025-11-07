// src/test/java/org/example/controller/DeepSeekServiceTest.java
package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeepSeekServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DeepSeekService deepSeekService;

    // 定义测试用的URL常量
    private static final String TEST_API_URL = "https://api.deepseek.com/chat/completions";

    @Before
    public void setUp() {
        // 使用反射设置测试URL，确保与代码中的URL一致
        try {
            java.lang.reflect.Field apiUrlField = DeepSeekService.class.getDeclaredField("apiUrl");
            apiUrlField.setAccessible(true);
            apiUrlField.set(deepSeekService, TEST_API_URL);
        } catch (Exception e) {
            // 如果反射失败，继续测试，可能URL已经在构造函数中设置
            System.err.println("警告: 无法通过反射设置apiUrl: " + e.getMessage());
        }
    }

    @Test
    public void testCallDeepSeekApi_Success() throws Exception {
        // 准备测试数据
        String input = "Hello, DeepSeek";
        String expectedResponse = "这是DeepSeek的回复";

        String mockApiResponse = "{" +
                "\"choices\": [{" +
                "\"message\": {" +
                "\"content\": \"" + expectedResponse + "\"" +
                "}" +
                "}]" +
                "}";

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(
                mockApiResponse, HttpStatus.OK
        );

        // 模拟RestTemplate行为 - 使用正确的URL
        when(restTemplate.exchange(
                eq(TEST_API_URL),  // 使用常量而不是硬编码的URL
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // 执行测试
        String result = deepSeekService.callDeepSeekApi(input);

        // 验证结果
        assertEquals("API调用应该返回预期的响应", expectedResponse, result);
    }

    @Test
    public void testCallDeepSeekApi_HttpClientError() {
        // 准备测试数据
        String input = "Test input";

        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized",
                "{\"error\": \"Invalid API key\"}".getBytes(),
                StandardCharsets.UTF_8
        );

        // 模拟RestTemplate抛出异常
        when(restTemplate.exchange(
                eq(TEST_API_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(exception);

        // 执行测试
        String result = deepSeekService.callDeepSeekApi(input);

        // 验证结果
        assertTrue("应该包含调用失败信息", result.contains("调用失败"));
    }

    @Test
    public void testCallDeepSeekApi_EmptyInput() throws Exception {
        // 准备测试数据
        String input = "";
        String expectedResponse = "Empty input response";

        String mockApiResponse = "{" +
                "\"choices\": [{" +
                "\"message\": {" +
                "\"content\": \"" + expectedResponse + "\"" +
                "}" +
                "}]" +
                "}";

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(
                mockApiResponse, HttpStatus.OK
        );

        when(restTemplate.exchange(
                eq(TEST_API_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // 执行测试
        String result = deepSeekService.callDeepSeekApi(input);

        // 验证结果
        assertEquals(expectedResponse, result);
    }

    @Test
    public void testCallDeepSeekApi_NullInput() throws Exception {
        // 准备测试数据
        String input = null;
        String expectedResponse = "Null input response";

        String mockApiResponse = "{" +
                "\"choices\": [{" +
                "\"message\": {" +
                "\"content\": \"" + expectedResponse + "\"" +
                "}" +
                "}]" +
                "}";

        ResponseEntity<String> mockResponseEntity = new ResponseEntity<>(
                mockApiResponse, HttpStatus.OK
        );

        when(restTemplate.exchange(
                eq(TEST_API_URL),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponseEntity);

        // 执行测试
        String result = deepSeekService.callDeepSeekApi(input);

        // 验证结果 - 应该能处理null输入
        assertNotNull("即使输入为null也应该有响应", result);
    }

    // 添加一个简单的测试，验证服务是否正常初始化
    @Test
    public void testServiceInitialization() {
        assertNotNull("DeepSeekService 应该被正确初始化", deepSeekService);
        assertNotNull("RestTemplate mock 应该被注入", restTemplate);
    }
}