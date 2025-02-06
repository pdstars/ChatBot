package com.ruoyi.layim.mapper;

import com.ruoyi.layim.domain.ChatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatUserMapper {

    @Select("select * from chat_user")
    public List<ChatUser> findAllChatUser();

    @Select("select * from chat_user where id = #{userId}")
    public ChatUser queryById(Long userId);


    public List<ChatUser> findUserByList(List<Long> list);
}
