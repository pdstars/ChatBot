package com.ruoyi.layim.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.layim.enums.OnlineStatus;

import java.util.Objects;

/**
 * layim通讯用户信息
 */
public class ChatUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 用户id*/
    private Long id;

    private String userName;

    private String sign;

    private String avatar;

    private OnlineStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public OnlineStatus getStatus() {
        return status;
    }


    public void setStatus(OnlineStatus status) {
        this.status = status;
    }

}
