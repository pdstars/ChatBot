package org.zhong.chatgpt.wechat.bot.util;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zhong.chatgpt.wechat.bot.model.BotMsg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 新闻
 */
@Service
public class NewsProcessor {
    @Autowired
    SysConfigServiceImpl configService;
    private static Logger LOG = LoggerFactory.getLogger(NewsProcessor.class);
    // 打算从另一个群里转发消息
//    public void process(BotMsg botMsg) {
//        try{
//            BaseMsg baseMsg = botMsg.getBaseMsg();
//            String groupName = baseMsg.getGroupName();
//            //判断群聊是否是目标群聊
//            if(newsgroup.equals(groupName)){
//                String text = baseMsg.getText() == null ? "": baseMsg.getText();
//                if(!text.isEmpty() && text.contains("每日微语报")){
//                    // 获取今日日期
//                    LocalDate today = LocalDate.now();
//                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    String date = today.format(dateTimeFormatter);
//                    String filePath = workspace + "/news/" + date + "/new.txt";
//                    File file = new File(filePath);
//                    if(!file.exists()){
//                        file.getParentFile().mkdirs();
//                        file.createNewFile();
//                    } else {
//                        LOG.info("今日新闻已保存");
//                        return;
//                    }
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//                    writer.write(removeTow11(text));
//                    writer.close();
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 过滤掉双11信息
     * @param text
     */

    public String removeTow11(String text){
        StringBuilder sb = new StringBuilder();
        //获取每一行的数据
        String[] textSplit = text.split("<br/><br/>");
        for(String o: textSplit){
            if(o.contains("双11")){
                continue;
            }
            sb.append(o + "<br/><br/>");
        }
        return sb.toString();
    }

    public void sendNews2Group(String groupName, Core core){
        try{
            String content = getNewsContent();
            MessageTools.sendMsgById(content, WechatTools.getGroupIdByNickName(groupName,core),core);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getNewsContent(){
        String content = "";
        try{
            LocalDate today = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String date = today.format(dateTimeFormatter);
            String filePath = configService.selectConfigByKey("bot.workspace") + "/news/" + date + "/new.txt";
            content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            content = content.replaceAll("<br/><br/>","\n");
            content = content.replaceAll("<br/>","\n");
        } catch (Exception e){
            e.printStackTrace();
        }
        return content;

    }


}
