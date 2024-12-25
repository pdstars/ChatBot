package com.ruoyi.quartz.task;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.core.CoreManage;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zhong.chatgpt.wechat.bot.service.MpWechatService;
import org.zhong.chatgpt.wechat.bot.util.NewsProcessor;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component("newTimer")
public class NewsTimer {
    @Autowired
    MpWechatService mpWechatService;

    @Autowired
    SysConfigServiceImpl configService;

    @Autowired
    NewsProcessor newsProcessor;

    private static Logger LOG = LoggerFactory.getLogger(NewsTimer.class);
    public void sendNews(String nickName,String groupName){
        Core core = CoreManage.getInstance().getCoreByNickName(nickName);
        LOG.info("每日新闻线程启动");
        //获取白名单群组
        String content = newsProcessor.getNewsContent();
        //循环100次获取数据
        for(int i = 0;i< 100;i++){
            // 如果没数据，继续获取
            if(!StringUtils.isEmpty(content)){
                break;
            } else {
                content = newsProcessor.getNewsContent();
            }
            if(!StringUtils.isEmpty(content)){
                break;
            }
            try {
                Thread.sleep(180000);
            } catch (Exception e){
                e.printStackTrace();
            }
            if(i==99){
                return;
            }
        }
        MessageTools.sendMsgById(content, WechatTools.getGroupIdByNickName(groupName,core),core);
    }


//    public void privateTalk(){
//        LOG.info("每日通知微信用户线程启动");
//        //获取白名单群组
//        List<String> whiteList =  botConfig.getUserWhiteList();
//        String path = botConfig.getWorkspace() + "/config/temp.txt";
//        File file = new File(path);
//        if(!file.exists()){
//            LOG.info("未配置发送消息");
//            return;
//        }
//        String content = FileUtil.readString(file,"utf-8");
//        if(StringUtils.isEmpty(content)){
//            LOG.info("发送消息为空");
//            return;
//        }
//        for(String o: whiteList){
//            MessageTools.sendMsgById(content,WechatTools.getUserNameByNickName(o));
//        }
//    }

    public void startGetNews() throws IOException, InterruptedException {
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd");
        String date = today.format(dateTimeFormatter);
        String text = "";
        try {
            JSONObject json = mpWechatService.getMpUrlJSON();
            text = mpWechatService.parseMpJson(json);
            if(!text.contains(date)){
                LOG.info("本次新闻爬取无内容");
                return;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        LOG.debug("保存的今日新闻为 \n" + text);
        LOG.info("今日新闻已保存");
        mpWechatService.saveMpText(text);
    }

}
