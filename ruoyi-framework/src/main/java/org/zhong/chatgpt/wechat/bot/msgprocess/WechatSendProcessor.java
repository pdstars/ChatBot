package org.zhong.chatgpt.wechat.bot.msgprocess;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.core.Core;
import org.zhong.chatgpt.wechat.bot.model.BotMsg;

public class WechatSendProcessor implements MsgProcessor{

	Core core;
	public WechatSendProcessor(Core core){
		this.core = core;
	}
	@Override
	public BotMsg process(BotMsg botMsg) {

		MessageTools.sendMsgById(botMsg.getReplyMsg(), botMsg.getBaseMsg().getFromUserName(),core);
		return null;
	}

}
