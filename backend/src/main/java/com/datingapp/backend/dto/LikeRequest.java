package com.datingapp.backend.dto;

import javax.validation.constraints.NotNull;

public class LikeRequest {

    @NotNull
    private Long fromUserId;

    @NotNull
    private Long toUserId;

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }
}
