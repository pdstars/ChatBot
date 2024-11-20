package org.zhong.chatgpt.wechat.bot.util;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

public class TianGProcessor {
    private String url = "https://apis.tianapi.com/tiangou/index?key=%s";


    private static Logger LOG = LoggerFactory.getLogger(TianGProcessor.class);
    public String process() {
        ISysConfigService configService = SpringUtil.getBean(SysConfigServiceImpl.class);
        String apiKey = configService.selectConfigByKey("bot.tianapi.apikey");
        String res = HttpClientUtils.get(String.format(this.url,apiKey));
        JSONObject json = JSONObject.parseObject(res);
        String result = "";
        if("200".equals(json.getString("code"))){
            JSONObject content = json.getJSONObject("result");
            result = content.getString("content");
        }else {
            LOG.error(String.format("舔狗日记获取失败【%s】",json.getString("msg")));
        }
        return result;
    }

}
