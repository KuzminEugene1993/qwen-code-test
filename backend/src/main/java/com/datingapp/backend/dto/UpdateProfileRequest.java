package com.datingapp.backend.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(max = 400)
    private String bio;

    @Min(18)
    @Max(100)
    private Integer age;

    @Size(max = 100)
    private String city;

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
