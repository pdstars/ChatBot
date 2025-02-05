package com.ruoyi.layim.service.impl;

import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.mapper.ChatUserMapper;
import com.ruoyi.layim.mapper.FriendMapper;
import com.ruoyi.layim.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatUserServiceImpl implements ChatUserService {
    @Autowired
    ChatUserMapper chatUserMapper;

    @Override
    public List<ChatUser> findAll() {
        return chatUserMapper.findAllChatUser();
    }


}
