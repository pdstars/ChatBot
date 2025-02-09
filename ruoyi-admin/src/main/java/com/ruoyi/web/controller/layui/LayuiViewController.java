package com.ruoyi.web.controller.layui;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.RequestUtil;
import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.service.ChatUserService;
import com.ruoyi.layim.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/layui")
public class LayuiViewController extends BaseController {
    @Autowired
    ChatUserService chatUserService;

    @Autowired
    FriendService friendService;
    @GetMapping("/demo")
    public String demo()
    {
        if(RequestUtil.checkAgentIsMobile()){
            return "demo/layim/mobile";
        }
        return "demo/layim/index";
    }
    @GetMapping("/user")
    @ResponseBody
    public R getAllUser()
    {
        return R.ok(chatUserService.findAll());
    }

    @GetMapping("/friend")
    @ResponseBody
    public R getFriendByuserId(@RequestParam Long userid)
    {
        return R.ok(friendService.getFriendByUserId(userid));
    }

}
