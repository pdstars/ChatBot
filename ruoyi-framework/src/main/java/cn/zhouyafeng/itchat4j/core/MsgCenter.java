package cn.zhouyafeng.itchat4j.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.face.IMsgHandlerFace;
import cn.zhouyafeng.itchat4j.utils.enums.MsgCodeEnum;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;

/**
 * 消息处理中心
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年5月14日 下午12:47:50
 * @version 1.0
 *
 */
public class MsgCenter {
	private static Logger LOG = LoggerFactory.getLogger(MsgCenter.class);

	private static Core core = new Core();

	private static Map<String, String> nickNameMap = new HashMap<String, String>();
	private static Map<String, String> groupNameMap = new HashMap<String, String>();
	/**
	 * 接收消息，放入队列
	 * 
	 * @author https://github.com/yaphone
	 * @date 2017年4月23日 下午2:30:48
	 * @param msgList
	 * @return
	 */
	public static JSONArray produceMsg(JSONArray msgList) {
		JSONArray result = new JSONArray();
		for (int i = 0; i < msgList.size(); i++) {
			JSONObject msg = new JSONObject();
			JSONObject m = msgList.getJSONObject(i);
			m.put("groupMsg", false);// 是否是群消息
			if (m.getString("FromUserName").contains("@@") || m.getString("ToUserName").contains("@@")) { // 群聊消息
				if (m.getString("FromUserName").contains("@@")
						&& !core.getGroupIdList().contains(m.getString("FromUserName"))) {
					core.getGroupIdList().add((m.getString("FromUserName")));
				} else if (m.getString("ToUserName").contains("@@")
						&& !core.getGroupIdList().contains(m.getString("ToUserName"))) {
					core.getGroupIdList().add((m.getString("ToUserName")));
				}
				// 群消息与普通消息不同的是在其消息体（Content）中会包含发送者id及":<br/>"消息，这里需要处理一下，去掉多余信息，只保留消息内容
				if (m.getString("Content").contains("<br/>")) {
					
					String contentText = m.getString("Content");
					String content = contentText.substring(contentText.indexOf("<br/>") + 5);
					String groupUserName = contentText.substring(0, contentText.indexOf(":<br/>"));
					String groupUserNickName = nickNameMap.get(groupUserName);
					String groupName = groupNameMap.get(m.getString("FromUserName"));
					
					if(StringUtils.isEmpty(groupUserNickName)) {
						JSONArray groupMembers = core.getGroupMemeberMap().get(m.getString("FromUserName"));
						int size = groupMembers.size();
						for(int j=0; j < size; j++) {
							JSONObject member = groupMembers.getJSONObject(j);
							if(member.getString("UserName").equals(groupUserName)) {
								groupUserNickName = member.getString("NickName");
								nickNameMap.put(groupUserName, groupUserNickName);
								break;
							}
						}
					}
					
					if(StringUtils.isEmpty(groupName)) {
						List<JSONObject> jsonObjects = core.getGroupList();
						for(JSONObject obj : jsonObjects) {
							if(obj.getString("UserName").equals(m.getString("FromUserName"))) {
								groupName = obj.getString("NickName");
								groupNameMap.put(m.getString("FromUserName"), groupName);
								break;
							}
						}
					}

					m.put("groupName", groupName);
					m.put("groupUserName", groupUserName);
					m.put("groupUserNickName", groupUserNickName);
					m.put("Content", content);
					m.put("groupMsg", true);
				}
			} else {
				String fromUserNickName = nickNameMap.get(m.getString("FromUserName"));
				if(StringUtils.isEmpty(fromUserNickName)) {
					Map<String, JSONObject> userInfoMap = core.getUserInfoMap();
					JSONObject userObj = userInfoMap.get(m.getString("FromUserName"));
					if(userObj != null) {
						fromUserNickName = userObj.getString("NickName");
					}
					
				}
				m.put("fromUserNickName", fromUserNickName);
				CommonTools.msgFormatter(m, "Content");
			}
			if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_TEXT.getCode())) { // words
																						// 文本消息
				if (m.getString("Url").length() != 0) {
					String regEx = "(.+?\\(.+?\\))";
					Matcher matcher = CommonTools.getMatcher(regEx, m.getString("Content"));
					String data = "Map";
					if (matcher.find()) {
						data = matcher.group(1);
					}
					msg.put("Type", "Map");
					msg.put("Text", data);
				} else {
					msg.put("Type", MsgTypeEnum.TEXT.getType());
					msg.put("Text", m.getString("Content"));
				}
				m.put("Type", msg.getString("Type"));
				m.put("Text", msg.getString("Text"));
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_IMAGE.getCode())
					|| m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_EMOTICON.getCode())) { // 图片消息
				m.put("Type", MsgTypeEnum.PIC.getType());
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VOICE.getCode())) { // 语音消息
				m.put("Type", MsgTypeEnum.VOICE.getType());
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VERIFYMSG.getCode())) {// friends
				// 好友确认消息
				// MessageTools.addFriend(core, userName, 3, ticket); // 确认添加好友
				m.put("Type", MsgTypeEnum.VERIFYMSG.getType());

			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SHARECARD.getCode())) { // 共享名片
				m.put("Type", MsgTypeEnum.NAMECARD.getType());

			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VIDEO.getCode())
					|| m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MICROVIDEO.getCode())) {// viedo
				m.put("Type", MsgTypeEnum.VIEDO.getType());
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MEDIA.getCode())) { // 多媒体消息
				m.put("Type", MsgTypeEnum.MEDIA.getType());
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_STATUSNOTIFY.getCode())) {// phone
				// init
				// 微信初始化消息

			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SYS.getCode())) {// 系统消息
				m.put("Type", MsgTypeEnum.SYS.getType());
			} else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_RECALLED.getCode())) { // 撤回消息

			} else {
				LOG.info("Useless msg");
			}
			LOG.info("收到消息一条，来自: " + m.getString("FromUserName"));
			result.add(m);
		}
		return result;
	}

	/**
	 * 消息处理
	 * 
	 * @author https://github.com/yaphone
	 * @date 2017年5月14日 上午10:52:34
	 * @param msgHandler
	 */
	public static void handleMsg(IMsgHandlerFace msgHandler) {
		while (true) {
			if (core.getMsgList().size() > 0 && core.getMsgList().get(0).getContent() != null) {
				if (core.getMsgList().get(0).getContent().length() > 0) {
					BaseMsg msg = core.getMsgList().get(0);
					if (msg.getType() != null) {
						try {
							if (msg.getType().equals(MsgTypeEnum.TEXT.getType())) {
								String result = msgHandler.textMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
							} else if (msg.getType().equals(MsgTypeEnum.PIC.getType())) {

								String result = msgHandler.picMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
								
							} else if (msg.getType().equals(MsgTypeEnum.VOICE.getType())) {
								String result = msgHandler.voiceMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
								
							} else if (msg.getType().equals(MsgTypeEnum.VIEDO.getType())) {
								String result = msgHandler.viedoMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
								
							} else if (msg.getType().equals(MsgTypeEnum.NAMECARD.getType())) {
								String result = msgHandler.nameCardMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
								
							} else if (msg.getType().equals(MsgTypeEnum.SYS.getType())) { // 系统消息
								msgHandler.sysMsgHandle(msg);
							} else if (msg.getType().equals(MsgTypeEnum.VERIFYMSG.getType())) { // 确认添加好友消息
								
								String result = msgHandler.verifyAddFriendMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result,
											core.getMsgList().get(0).getRecommendInfo().getUserName(),core);
								}
								
							} else if (msg.getType().equals(MsgTypeEnum.MEDIA.getType())) { // 多媒体消息
								String result = msgHandler.mediaMsgHandle(msg);
								if(StringUtils.isNotEmpty(result)) {
									MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName(),core);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				core.getMsgList().remove(0);
			}
			try {
				TimeUnit.MILLISECONDS.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
