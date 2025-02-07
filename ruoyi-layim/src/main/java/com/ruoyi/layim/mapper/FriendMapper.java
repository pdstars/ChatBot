package com.ruoyi.layim.mapper;

import com.ruoyi.layim.domain.Friend;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendMapper {
    @Select("select * from chat_friend f left join chat_user u on f.friend = u.id where f.userid = #{userid}")
    public List<Friend> getFriendByUserId(Long userid);

    @Insert("insert into chat_friend values (#{id},#{userId},#{friend},#{groupName})")
    public void makeFriend(Friend friend);
}
