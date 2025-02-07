package com.ruoyi.layim.mapper;

import com.ruoyi.layim.domain.ChatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatUserMapper {

    @Select("select * from chat_user")
    public List<ChatUser> findAllChatUser();

    @Select("select * from chat_user where id = #{userId}")
    public ChatUser queryById(Long userId);


    public List<ChatUser> findUserByList(List<Long> list);

    @Update("update chat_user set status = #{status} where id = #{id}")
    public void setStatus(@Param("status") String status, @Param("id")  Long id);
}
