package org.zhong.chatgpt.wechat.bot.msgprocess;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.extra.spring.SpringUtil;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import com.ruoyi.framework.web.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhong.chatgpt.wechat.bot.config.BotConfig;
import org.zhong.chatgpt.wechat.bot.consts.BotConst;
import org.zhong.chatgpt.wechat.bot.model.BotMsg;
import org.zhong.chatgpt.wechat.bot.model.WehchatMsgQueue;
import org.zhong.chatgpt.wechat.bot.sensitive.SensitiveWord;

/**
 * 这是消息提前处理线程
 */
public class MsgPreProcessor implements MsgProcessor{

	TimedCache<String, String> timedCache = CacheUtil.newTimedCache(20*60*1000);
	private static Logger LOG = LoggerFactory.getLogger(MsgPreProcessor.class);

	Core core;

	public MsgPreProcessor(Core core){
		this.core = core;
	}
	public BotMsg process(BotMsg botMsg) {
		BaseMsg baseMsg = botMsg.getBaseMsg();
		String fromUserNickName = baseMsg.getFromUserNickName();
		if(StringUtils.isNotEmpty(fromUserNickName)
				&&(	fromUserNickName.contains("微信支付")
						|| fromUserNickName.contains("文件传输助手")
						|| fromUserNickName.contains("微信团队"))) {
			//忽略系统消息
			return null;
		}
		
		if((baseMsg.isGroupMsg() && timedCache.get(baseMsg.getGroupUserName()) != null)
				|| (!baseMsg.isGroupMsg() && timedCache.get(baseMsg.getFromUserName()) != null)) {
			return null;
		}

		if(baseMsg.isGroupMsg()) {//群聊
			//为了调试日志，在此记录
			LOG.info(String.format("收到群聊信息【%s】",botMsg.getBaseMsg().getText()));
			LOG.info(String.format("群聊名称是【%s】",botMsg.getBaseMsg().getGroupName()));
			
			if(!baseMsg.getContent().contains("@" + core.getNickName())) {
				//如果不是@我的消息
				return null;
			}
		}else {//私聊
		}
		if (baseMsg.getType().equals(MsgTypeEnum.TEXT.getType())) {
			baseMsg.setContent(baseMsg.getContent().replace("@" + core.getNickName(), ""));
			String content = baseMsg.getContent();
			if(StringUtils.isEmpty(content)) {
				//丢弃
				return null;
			}
		}else {
			return null;
		}
		return botMsg;
	}
}
