package com.ruoyi.layim.mapper;

import com.ruoyi.layim.domain.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {

    @Select("select * from chat_group where userid = #{userid}")
    public List<Group> queryGroupByUserId(Long userid);

    @Select("select * from chat_group cg left join chat_user cu on cg.userid = cu.id  where cg.id = #{id}")
    public List<Group> queryMember(Long id);
}
