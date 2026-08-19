package com.datingapp.backend.service;

import com.datingapp.backend.model.Match;
import com.datingapp.backend.model.Profile;
import com.datingapp.backend.model.User;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class DatingService {

    private final AtomicLong userIdGenerator = new AtomicLong(0);
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Profile> profiles = new HashMap<>();
    private final Map<Long, Set<Long>> likesByUser = new HashMap<>();
    private final Set<String> matchKeys = new HashSet<>();

    public User createUser(String email, String name) {
        Long id = userIdGenerator.incrementAndGet();
        User user = new User(id, email, name);
        users.put(id, user);
        profiles.put(id, new Profile(id, "", null, ""));
        return user;
    }

    public List<User> getUsers() {
        List<User> list = new ArrayList<>(users.values());
        list.sort(Comparator.comparing(User::getId));
        return list;
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public Profile getProfile(Long userId) {
        return profiles.get(userId);
    }

    public Profile upsertProfile(Long userId, String bio, Integer age, String city) {
        Profile profile = profiles.get(userId);
        if (profile == null) {
            return null;
        }

        if (bio != null) {
            profile.setBio(bio);
        }
        if (age != null) {
            profile.setAge(age);
        }
        if (city != null) {
            profile.setCity(city);
        }
        return profile;
    }

    public boolean existsUser(Long userId) {
        return users.containsKey(userId);
    }

    public boolean addLike(Long fromUserId, Long toUserId) {
        likesByUser.computeIfAbsent(fromUserId, id -> new HashSet<>()).add(toUserId);

        Set<Long> reverseLikes = likesByUser.get(toUserId);
        boolean isMatch = reverseLikes != null && reverseLikes.contains(fromUserId);
        if (isMatch) {
            matchKeys.add(buildMatchKey(fromUserId, toUserId));
        }
        return isMatch;
    }

    public List<Match> getMatches(Long userId) {
        List<Match> result = new ArrayList<>();
        for (String key : matchKeys) {
            String[] parts = key.split(":");
            Long a = Long.valueOf(parts[0]);
            Long b = Long.valueOf(parts[1]);
            if (a.equals(userId) || b.equals(userId)) {
                result.add(new Match(a, b));
            }
        }
        return result;
    }

    private String buildMatchKey(Long a, Long b) {
        long min = Math.min(a, b);
        long max = Math.max(a, b);
        return min + ":" + max;
    }
}
