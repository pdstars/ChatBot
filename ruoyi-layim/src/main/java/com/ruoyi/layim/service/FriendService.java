package com.ruoyi.layim.service;

import com.ruoyi.layim.domain.Friend;

import java.util.List;

public interface FriendService {
    // 根据用户名获取朋友信息
    public List<Friend> getFriendByUserId(Long userId);
}
