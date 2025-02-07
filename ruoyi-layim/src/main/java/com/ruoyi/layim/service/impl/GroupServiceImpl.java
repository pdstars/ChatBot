package com.ruoyi.layim.service.impl;

import com.ruoyi.layim.domain.Group;
import com.ruoyi.layim.mapper.GroupMapper;
import com.ruoyi.layim.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupMapper groupMapper;
    @Override
    public List<Group> queryGroupByUserId(Long userId) {
        return groupMapper.queryGroupByUserId(userId);
    }

    @Override
    public List<Group> queryGroupMember(Long id) {
        return groupMapper.queryMember(id);
    }
}
