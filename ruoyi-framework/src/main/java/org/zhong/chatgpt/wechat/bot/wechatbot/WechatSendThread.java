package org.zhong.chatgpt.wechat.bot.wechatbot;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zhong.chatgpt.wechat.bot.model.BotMsg;
import org.zhong.chatgpt.wechat.bot.model.WehchatMsgQueue;
import org.zhong.chatgpt.wechat.bot.msgprocess.MsgProcessor;

public class WechatSendThread implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(WechatSendThread.class);
	
	private MsgProcessor msgProcessor;
	
	public WechatSendThread(MsgProcessor msgProcessor) {
		super();
		this.msgProcessor = msgProcessor;
	}

	@Override
	public void run() {
		for(;;) {
			BotMsg botMsg = WehchatMsgQueue.popSendMsg();
			if(botMsg == null) {
				logger.debug("从waitSendMsgs获取消息为空");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				logger.debug("从waitSendMsgs获取消息:{}", JSON.toJSONString(botMsg));
				msgProcessor.process(botMsg);
			}
		}
		
	}

	public static void start(MsgProcessor msgProcessor) {
		logger.info("消息发送线程启动");
		new Thread(new WechatSendThread(msgProcessor)).start();
	}
}
