package com.ruoyi.web.controller.layui;

import com.ruoyi.common.core.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/layui")
public class LayuiViewController extends BaseController {

    @GetMapping("/demo")
    public String demo()
    {
        return "demo/layim/index";
    }

}
