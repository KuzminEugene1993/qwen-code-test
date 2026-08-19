package com.datingapp.backend.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/api/info")
    public Map<String, String> info() {
        return Map.of(
            "message", "Dating backend is running",
            "health", "/api/health",
            "frontend", "http://localhost:5173"
        );
    }
}
