package com.king.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class KickRequest {

    public KickRequest(Long groupId, Set<Long> toKickIds, String message) {
        this.groupId = groupId;
        this.toKickIds = toKickIds;
        this.message = message;
    }

    private Long groupId;
    private Set<Long> toKickIds;

    private Set<Long> kickSuccessIds = new HashSet<>();
    private Set<Long> kickFailIds = new HashSet<>();

    private String message="";
    private boolean block=false;
    private int countTop=5;
}
