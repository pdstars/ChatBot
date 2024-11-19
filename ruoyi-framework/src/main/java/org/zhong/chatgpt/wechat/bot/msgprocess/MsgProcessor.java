package org.zhong.chatgpt.wechat.bot.msgprocess;

import cn.zhouyafeng.itchat4j.core.Core;
import org.zhong.chatgpt.wechat.bot.model.BotMsg;

public interface MsgProcessor {

	public void process(BotMsg botMsg);
}
