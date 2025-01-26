package com.ruoyi.web.controller.layui;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.service.ChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/layui")
public class LayuiViewController extends BaseController {
    @Autowired
    ChatUserService chatUserService;
    @GetMapping("/demo")
    public String demo()
    {
        return "demo/layim/index";
    }
    @GetMapping("/user")
    @ResponseBody
    public R test()
    {
        return R.ok(chatUserService.findAll());
    }

}
