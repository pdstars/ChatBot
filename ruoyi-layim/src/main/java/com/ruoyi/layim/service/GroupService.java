package com.ruoyi.layim.service;

import com.ruoyi.layim.domain.Group;

import java.util.List;

public interface GroupService {

    public List<Group> queryGroupByUserId(Long userId);

    public List<Group> queryGroupMember(Long id);
}
