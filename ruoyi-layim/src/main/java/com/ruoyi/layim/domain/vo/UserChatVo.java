package com.ruoyi.layim.domain.vo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.layim.utils.LayimUtils;

/**
 * 用户发送消息的视图
 */
public class UserChatVo {
    //来源用户名
    private String username;
    //来源id
    private String id;

    private String avatar;

    private String type;
    private String content;
    private String cid;
    private boolean mine;

    private String fromid;
    private Long timestamp;

    public UserChatVo(String message){
        JSONObject from = LayimUtils.getMine(message);
        this.username=from.getString("username");
        this.avatar = from.getString("avatar");
        this.content=from.getString("content");
        this.fromid=from.getString("id");
        this.id=from.getString("id");
        JSONObject to = LayimUtils.getToUser(message);
        this.type = to.getString("type");
        this.timestamp=System.currentTimeMillis();
    }

    public UserChatVo(String message,String group){
        JSONObject from = LayimUtils.getMine(message);
        this.content=from.getString("content");
        JSONObject to = LayimUtils.getToUser(message);
        this.fromid=from.getString("id");
        this.avatar = to.getString("avatar");
        this.id=to.getString("id");
        this.username=from.getString("username");
        this.type = to.getString("type");
        this.timestamp=System.currentTimeMillis();
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
