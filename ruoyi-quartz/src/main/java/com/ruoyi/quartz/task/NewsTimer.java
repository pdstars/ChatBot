package com.ruoyi.quartz.task;

import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.core.CoreManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("newTimer")
public class NewsTimer {

    private static Logger LOG = LoggerFactory.getLogger(NewsTimer.class);
    public void sendNews(String nickName){
        Core core = CoreManage.getInstance().getCoreByNickName(nickName);
//        LOG.info("每日新闻线程启动");
//        //获取白名单群组
//        List<String> whiteList =  botConfig.getGroupWhiteList();
//        String content = newsProcessor.getNewsContent();
//
//        //循环100次获取数据
//        for(int i = 0;i< 100;i++){
//            // 如果没数据，继续获取
//            if(!StringUtils.isEmpty(content)){
//                break;
//            } else {
//                content = newsProcessor.getNewsContent();
//            }
//            try {
//                Thread.sleep(600000);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//            if(i==99){
//                return;
//            }
//        }
//        for(String o: whiteList){
//            MessageTools.sendMsgById(content, WechatTools.getGroupIdByNickName(o));
//        }
    }


//    @Scheduled(cron = "0 0 6 * * ?")
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

//    @Scheduled(cron = "0 20 6 * * ?")
//    public void startGetNews() throws IOException {
//        LOG.info("每日爬取公众号新闻线程启动");
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd");
//        String date = today.format(dateTimeFormatter);
//        String text = "";
//        for(int i = 0;i< 100;i++){
//            try {
//                LOG.info("第" + i + "次爬取新闻");
//                JSONObject json = mpWechatService.getMpUrlJSON();
//                LOG.info("爬取到的数据为" + json);
//                text = mpWechatService.parseMpJson(json);
//                if(text.contains(date)){
//                    break;
//                }
//                Thread.sleep(300000);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//            if(i==99){
//                return;
//            }
//
//        }
//        mpWechatService.saveMpText(text);
//    }

}
