package com.ruoyi.layim.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.Friend;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LayimUtils {
    public static Map<Long,List<Long>> getIds4List(List<Friend> list){
        Map<Long,List<Long>> result = new HashMap<>();
        for (Friend f:list) {
            List<Long> itemList = result.get(f.getId());
            if(ObjectUtils.isEmpty(itemList)){
                itemList = new ArrayList<>();
                result.put(f.getId(),itemList);
            }
            itemList.add(f.getFriend());
        }
        return result;
    }

    public static Map<Long,String> getGroupNameMap(List<Friend> list){
        Map<Long,String> result = new HashMap<>();
        for(Friend f: list){
            result.put(f.getId(),f.getGroupName());
        }
        return result;
    }

    /**
     * 解析消息，获取发送人消息
     * @param message
     * @return
     */
    public static JSONObject getMine(String message){
        JSONObject json = JSON.parseObject(message);
        JSONObject data = json.getJSONObject("data");
        return data.getJSONObject("mine");
    };

    /**
     * 解析消息，获取发送的消息
     * @param message
     * @return
     */
    public static String getMessage(String message){
        JSONObject mine = LayimUtils.getMine(message);
        return mine.getString("content");
    };

    /**
     * 解析消息，获取发送放
     * @param message
     * @return
     */
    public static JSONObject getToUser(String message){
        JSONObject json = JSON.parseObject(message);
        JSONObject data = json.getJSONObject("data");
        return data.getJSONObject("to");
    };
}
