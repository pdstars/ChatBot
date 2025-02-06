package com.ruoyi.layim.domain;

import java.util.List;

public class FriendGroup {
    private String groupName;
    private Long id;

    private List<ChatUser> list;

    public String getgroupname() {
        return groupName;
    }

    public void setgroupname(String groupName) {
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ChatUser> getList() {
        return list;
    }

    public void setList(List<ChatUser> list) {
        this.list = list;
    }

    //public static FriendGroup
}
