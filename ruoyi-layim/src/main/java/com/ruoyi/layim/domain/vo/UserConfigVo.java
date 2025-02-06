package com.ruoyi.layim.domain.vo;

import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.FriendGroup;
import com.ruoyi.layim.domain.Group;

import java.util.ArrayList;
import java.util.List;


public class UserConfigVo {
    private ChatUser mine;

    private List<FriendGroup> friend = new ArrayList<>();

    private List<Group> group;
    public ChatUser getMine() {
        return mine;
    }

    public void setMine(ChatUser mine) {
        this.mine = mine;
    }

    public List<FriendGroup> getFriend() {
        return friend;
    }

    public void setFriend(List<FriendGroup> friend) {
        this.friend = friend;
    }

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
    }

    public void addFriendGroup(FriendGroup friendGroup){
        this.friend.add(friendGroup);
    }
}
