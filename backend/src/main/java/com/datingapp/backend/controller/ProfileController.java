package com.datingapp.backend.controller;

import com.datingapp.backend.dto.UpdateProfileRequest;
import com.datingapp.backend.model.Profile;
import com.datingapp.backend.service.DatingService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final DatingService datingService;

    public ProfileController(DatingService datingService) {
        this.datingService = datingService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfile(@PathVariable Long userId) {
        Profile profile = datingService.getProfile(userId);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Profile> updateProfile(
        @PathVariable Long userId,
        @Valid @RequestBody UpdateProfileRequest request
    ) {
        Profile updated = datingService.upsertProfile(
            userId,
            request.getBio(),
            request.getAge(),
            request.getCity()
        );

        if (updated == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updated);
    }
}
