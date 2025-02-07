package com.ruoyi.layim.service;

import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.vo.UserConfigVo;

import java.util.List;

public interface ChatUserService {

    public List<ChatUser> findAll();

    public ChatUser queryByUserId(Long userId);

    public UserConfigVo getUserConfig(Long userId);

    public void setStatus(String status,Long id);

}
