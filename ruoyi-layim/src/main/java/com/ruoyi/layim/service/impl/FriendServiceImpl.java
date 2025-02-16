package com.ruoyi.layim.service.impl;

import com.ruoyi.layim.domain.Friend;
import com.ruoyi.layim.mapper.FriendMapper;
import com.ruoyi.layim.service.FriendService;
import com.ruoyi.layim.utils.SnowFlakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    FriendMapper friendMapper;

    @Override
    public List<Friend> getFriendByUserId(Long userId) {
        return friendMapper.getFriendByUserId(userId);
    }

    @Override
    public void addFriend(Friend friend) {
        friend.setId(SnowFlakeUtil.getID());
        friendMapper.makeFriend(friend);
    }
}
