package com.ruoyi.layim.service.impl;

import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.Friend;
import com.ruoyi.layim.domain.FriendGroup;
import com.ruoyi.layim.domain.Group;
import com.ruoyi.layim.domain.vo.UserConfigVo;
import com.ruoyi.layim.mapper.ChatUserMapper;
import com.ruoyi.layim.mapper.FriendMapper;
import com.ruoyi.layim.service.ChatUserService;
import com.ruoyi.layim.service.FriendService;
import com.ruoyi.layim.service.GroupService;
import com.ruoyi.layim.utils.LayimUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ChatUserServiceImpl implements ChatUserService {
    @Autowired
    ChatUserMapper chatUserMapper;

    @Autowired
    FriendService friendService;

    @Autowired
    GroupService groupService;

    private static final Logger LOG = LoggerFactory.getLogger(ChatUserServiceImpl.class);

    @Override
    public List<ChatUser> findAll() {
        return chatUserMapper.findAllChatUser();
    }

    @Override
    public ChatUser queryByUserId(Long userId) {
        return chatUserMapper.queryById(userId);
    }

    @Override
    public UserConfigVo getUserConfig(Long userId) {
        UserConfigVo result = new UserConfigVo();
        // 获取用户信息
        ChatUser chatUser = chatUserMapper.queryById(userId);
        if(ObjectUtils.isEmpty(chatUser)){
            LOG.error("没找到此id的用户");
            return result;
        }
        result.setMine(chatUser);
        // 获取用户的friend 信息
        List<Friend> friendList = friendService.getFriendByUserId(userId);
        Map<Long,List<Long>> friendGroup = LayimUtils.getIds4List(friendList);
        Set<Long> keys = friendGroup.keySet();
        Map<Long,String> groupNameMap = LayimUtils.getGroupNameMap(friendList);
        for(Long o: keys){
            List<Long> ids = friendGroup.get(o);
            List<ChatUser> chatUserList = chatUserMapper.findUserByList(ids);
            FriendGroup friendGroupTemp = new FriendGroup();
            friendGroupTemp.setId(o);
            friendGroupTemp.setList(chatUserList);
            friendGroupTemp.setgroupname(groupNameMap.get(o));
            result.addFriendGroup(friendGroupTemp);
        }
        List<Group> groupList = groupService.queryGroupByUserId(userId);
        result.setGroup(groupList);
        return result;
    }

    @Override
    public void setStatus(String status, Long id) {
        chatUserMapper.setStatus(status,id);
    }


}
