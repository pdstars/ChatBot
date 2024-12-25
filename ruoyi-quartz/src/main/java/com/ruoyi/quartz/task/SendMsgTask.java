package com.ruoyi.quartz.task;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.core.CoreManage;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("SendMsgTask")
public class SendMsgTask {
    private static Logger LOG = LoggerFactory.getLogger(SendMsgTask.class);
    public void sendNews2Group(String nickName,String groupName,String msg){
        Core core = CoreManage.getInstance().getCoreByNickName(nickName);
        msg = msg.replace("\\n","\n");
        LOG.info(String.format("发送消息%s",msg));
        MessageTools.sendMsgById(msg, WechatTools.getGroupIdByNickName(groupName,core),core);
    }
}
