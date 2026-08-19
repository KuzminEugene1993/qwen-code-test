package com.datingapp.backend.controller;

import com.datingapp.backend.model.Match;
import com.datingapp.backend.service.DatingService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final DatingService datingService;

    public MatchController(DatingService datingService) {
        this.datingService = datingService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Match>> getMatches(@PathVariable Long userId) {
        if (!datingService.existsUser(userId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(datingService.getMatches(userId));
    }
}
