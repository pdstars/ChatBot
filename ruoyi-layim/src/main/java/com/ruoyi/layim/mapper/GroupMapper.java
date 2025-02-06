package com.ruoyi.layim.mapper;

import com.ruoyi.layim.domain.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {

    @Select("select * from chat_group where userid = #{userid}")
    public List<Group> queryGroupByUserId(Long userid);
}
