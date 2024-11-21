package com.ruoyi.web.controller.bot;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.core.CoreManage;
import cn.zhouyafeng.itchat4j.service.ILoginService;
import cn.zhouyafeng.itchat4j.service.impl.LoginServiceImpl;
import cn.zhouyafeng.itchat4j.thread.CheckLoginStatusThread;
import cn.zhouyafeng.itchat4j.utils.SleepUtils;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;

import com.ruoyi.common.core.controller.BaseController;

import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.zhong.chatgpt.wechat.bot.entity.CoreVo;
import org.zhong.chatgpt.wechat.bot.model.Bot;

import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * 表格相关
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/bot")
public class BotModelController extends BaseController
{
    private String prefix = "bot";

    private static final Logger LOG = LoggerFactory.getLogger(BotModelController.class);

    ILoginService loginService = new LoginServiceImpl();
    /**
     * 动态增删改查
     */
    @GetMapping("/curd")
    public String curd()
    {
        return prefix + "/curd";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    /**
     * 查询数据
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list()
    {
        TableDataInfo rspData = new TableDataInfo();
        List<Core> coreList = CoreManage.getInstance().getCoreList();

        List<CoreVo> coreVoList = new ArrayList<>();
        for(Core c: coreList){
            CoreVo cv = new CoreVo();
            cv.setByCore(c);
            coreVoList.add(cv);
        }
        // 查询条件过滤

        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (null == pageDomain.getPageNum() || null == pageDomain.getPageSize())
        {
            rspData.setRows(coreVoList);
            rspData.setTotal(coreVoList.size());
            return rspData;
        }
        Integer pageNum = (pageDomain.getPageNum() - 1) * 10;
        Integer pageSize = pageDomain.getPageNum() * 10;
        if (pageSize > coreVoList.size())
        {
            pageSize = coreVoList.size();
        }
        rspData.setRows(coreVoList.subList(pageNum, pageSize));
        rspData.setTotal(coreVoList.size());
        return rspData;
    }
    /**
     * 查看详细
     */
    @GetMapping("/detail/{userName}")
    public String detail(@PathVariable("userName") String userName, ModelMap mmap)
    {
        mmap.put("core", CoreManage.getInstance().getCoreByUserName(userName));
        return prefix + "/detail";
    }
    @GetMapping("/getLoginQrCode")
    public void login( HttpServletResponse response) {
        Core core = new Core();
        long timeout = 120000; // 设置超时时间为10秒
        long startTime = System.currentTimeMillis();
        while (true) {
            for (int count = 0; count < 10; count++) {
                LOG.info("获取UUID");
                while (loginService.getUuid(core) == null) {
                    LOG.info("1. 获取微信UUID");
                    while (loginService.getUuid(core) == null) {
                        LOG.warn("1.1. 获取微信UUID失败，两秒后重新获取");
                        SleepUtils.sleep(2000);
                    }
                }
                LOG.info("2. 获取登陆二维码图片");
                HttpEntity httpEntity = loginService.getQR(core);
                if (httpEntity!= null) {
                    try{   // 设置响应的类型为图片
                        byte[] bytes = EntityUtils.toByteArray(httpEntity);
                        System.out.println(bytes.length);
                        response.getOutputStream().write(bytes);
                        response.getOutputStream().close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                } else if (count == 10) {
                    LOG.error("2.2. 获取登陆二维码图片失败，系统退出");
                    return;
                }
            }
            LOG.info("3. 请扫描二维码图片，并在手机上确认");
            if (!core.isAlive()) {
                loginService.login(core);
                core.setAlive(true);
                LOG.info(("登陆成功"));
                break;
            }

            LOG.info("4. 登陆超时，请重新扫描二维码图片");
            return;
        }
        Bot.buildMyAiWehatBot(core);
        LOG.info("5. 登陆成功，微信初始化");
        if (!loginService.webWxInit(core)) {
            LOG.info("6. 微信初始化异常");
            System.exit(0);
        }

        LOG.info("6. 开启微信状态通知");
        loginService.wxStatusNotify(core);

        LOG.info("7. 清除。。。。");
        CommonTools.clearScreen();
        LOG.info(String.format("欢迎回来， %s", core.getNickName()));

        LOG.info("8. 开始接收消息");
        loginService.startReceiving(core);

        LOG.info("9. 获取联系人信息");
        loginService.webWxGetContact(core);

        LOG.info("10. 获取群好友及群好友列表");
        loginService.WebWxBatchGetContact(core);

        LOG.info("11. 缓存本次登陆好友相关消息");
        WechatTools.setUserInfo(core); // 登陆成功后缓存本次登陆好友相关消息（NickName, UserName）

        LOG.info("12.开启微信状态检测线程");

        // 给coreManage加入core
        CoreManage.getInstance().putCore(core);
        CheckLoginStatusThread checkLoginStatusThread = new CheckLoginStatusThread(core);
        new Thread(checkLoginStatusThread).start();
    }
}
