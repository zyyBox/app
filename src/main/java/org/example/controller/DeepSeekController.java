package org.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DeepSeekController {
    @Autowired
    private DeepSeekService deepSeekService;

    @PostMapping("/call-deepseek")
    public String callDeepSeek(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        return deepSeekService.callDeepSeekApi(input);
    }
}
