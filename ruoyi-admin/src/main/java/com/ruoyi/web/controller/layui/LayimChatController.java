package com.ruoyi.web.controller.layui;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.vo.UserConfigVo;
import com.ruoyi.layim.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/layim")
public class LayimChatController {
    @Autowired
    ChatUserService chatUserService;
    @RequestMapping("userInfo")
    @ResponseBody
    public R userInfo(Long userId){
        UserConfigVo userConfigVo = chatUserService.getUserConfig(userId);
        return R.ok(userConfigVo);
    }
}
