package com.ruoyi.layim.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class Friend extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long friend;

    private String groupName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriend() {
        return friend;
    }

    public void setFriend(Long friend) {
        this.friend = friend;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
