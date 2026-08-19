package com.datingapp.backend.model;

public class Profile {

    private Long userId;
    private String bio;
    private Integer age;
    private String city;

    public Profile() {
    }

    public Profile(Long userId, String bio, Integer age, String city) {
        this.userId = userId;
        this.bio = bio;
        this.age = age;
        this.city = city;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
