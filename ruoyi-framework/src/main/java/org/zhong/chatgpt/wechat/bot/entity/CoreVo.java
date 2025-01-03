package org.zhong.chatgpt.wechat.bot.entity;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreVo {
    boolean alive = false;
    private int memberCount = 0;

    private String indexUrl;

    private String userName;
    private String nickName;
    private List<BaseMsg> msgList = new ArrayList<BaseMsg>();

    private JSONObject userSelf; // 登陆账号自身信息
    private List<JSONObject> memberList = new ArrayList<JSONObject>(); // 好友+群聊+公众号+特殊账号
    private List<JSONObject> contactList = new ArrayList<JSONObject>();// 好友
    private List<JSONObject> groupList = new ArrayList<JSONObject>();; // 群
    private Map<String, JSONArray> groupMemeberMap = new HashMap<String, JSONArray>(); // 群聊成员字典
    private List<JSONObject> publicUsersList = new ArrayList<JSONObject>();;// 公众号／服务号
    private List<JSONObject> specialUsersList = new ArrayList<JSONObject>();;// 特殊账号
    private List<String> groupIdList = new ArrayList<String>(); // 群ID列表
    private List<String> groupNickNameList = new ArrayList<String>(); // 群NickName列表

    private Map<String, JSONObject> userInfoMap = new HashMap<String, JSONObject>();

    Map<String, Object> loginInfo = new HashMap<String, Object>();
    // CloseableHttpClient httpClient = HttpClients.createDefault();
    String uuid = null;

    boolean useHotReload = false;
    String hotReloadDir = "itchat.pkl";
    int receivingRetryCount = 5;

    private long lastNormalRetcodeTime; // 最后一次收到正常retcode的时间，秒为单位


    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<BaseMsg> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<BaseMsg> msgList) {
        this.msgList = msgList;
    }

    public JSONObject getUserSelf() {
        return userSelf;
    }

    public void setUserSelf(JSONObject userSelf) {
        this.userSelf = userSelf;
    }

    public List<JSONObject> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<JSONObject> memberList) {
        this.memberList = memberList;
    }

    public List<JSONObject> getContactList() {
        return contactList;
    }

    public void setContactList(List<JSONObject> contactList) {
        this.contactList = contactList;
    }

    public List<JSONObject> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<JSONObject> groupList) {
        this.groupList = groupList;
    }

    public Map<String, JSONArray> getGroupMemeberMap() {
        return groupMemeberMap;
    }

    public void setGroupMemeberMap(Map<String, JSONArray> groupMemeberMap) {
        this.groupMemeberMap = groupMemeberMap;
    }

    public List<JSONObject> getPublicUsersList() {
        return publicUsersList;
    }

    public void setPublicUsersList(List<JSONObject> publicUsersList) {
        this.publicUsersList = publicUsersList;
    }

    public List<JSONObject> getSpecialUsersList() {
        return specialUsersList;
    }

    public void setSpecialUsersList(List<JSONObject> specialUsersList) {
        this.specialUsersList = specialUsersList;
    }

    public List<String> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<String> groupIdList) {
        this.groupIdList = groupIdList;
    }

    public List<String> getGroupNickNameList() {
        return groupNickNameList;
    }

    public void setGroupNickNameList(List<String> groupNickNameList) {
        this.groupNickNameList = groupNickNameList;
    }

    public Map<String, JSONObject> getUserInfoMap() {
        return userInfoMap;
    }

    public void setUserInfoMap(Map<String, JSONObject> userInfoMap) {
        this.userInfoMap = userInfoMap;
    }

    public Map<String, Object> getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(Map<String, Object> loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isUseHotReload() {
        return useHotReload;
    }

    public void setUseHotReload(boolean useHotReload) {
        this.useHotReload = useHotReload;
    }

    public String getHotReloadDir() {
        return hotReloadDir;
    }

    public void setHotReloadDir(String hotReloadDir) {
        this.hotReloadDir = hotReloadDir;
    }

    public int getReceivingRetryCount() {
        return receivingRetryCount;
    }

    public void setReceivingRetryCount(int receivingRetryCount) {
        this.receivingRetryCount = receivingRetryCount;
    }

    public long getLastNormalRetcodeTime() {
        return lastNormalRetcodeTime;
    }

    public void setLastNormalRetcodeTime(long lastNormalRetcodeTime) {
        this.lastNormalRetcodeTime = lastNormalRetcodeTime;
    }

    public void setByCore(Core core){
        this.alive=core.isAlive();
        this.groupIdList = core.getGroupIdList();
        this.uuid = core.getUuid();
        this.memberList = core.getMemberList();
        this.nickName = core.getNickName();
        this.userName = core.getUserName();
    }
}
