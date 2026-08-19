package com.datingapp.backend.controller;

import com.datingapp.backend.dto.LikeRequest;
import com.datingapp.backend.service.DatingService;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final DatingService datingService;

    public LikeController(DatingService datingService) {
        this.datingService = datingService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> like(@Valid @RequestBody LikeRequest request) {
        if (request.getFromUserId().equals(request.getToUserId())) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "User cannot like themselves"
            ));
        }

        if (!datingService.existsUser(request.getFromUserId()) || !datingService.existsUser(request.getToUserId())) {
            return ResponseEntity.notFound().build();
        }

        boolean match = datingService.addLike(request.getFromUserId(), request.getToUserId());
        return ResponseEntity.ok(Map.of("match", match));
    }
}
