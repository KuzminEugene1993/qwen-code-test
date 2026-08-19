package com.datingapp.backend.model;

public class Match {

    private Long userA;
    private Long userB;

    public Match() {
    }

    public Match(Long userA, Long userB) {
        this.userA = userA;
        this.userB = userB;
    }

    public Long getUserA() {
        return userA;
    }

    public void setUserA(Long userA) {
        this.userA = userA;
    }

    public Long getUserB() {
        return userB;
    }

    public void setUserB(Long userB) {
        this.userB = userB;
    }
}
