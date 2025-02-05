package com.ruoyi.layim.test;

import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.Friend;

public class main {
    public static void main(String[] args) {
        Friend friend = new Friend();
        friend.setUserId(1L);
        Long t = (Long)friend.getUserId();
        System.out.println(t);
    }
}
