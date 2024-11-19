package com.ruoyi.web.controller.bot;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.service.ILoginService;
import cn.zhouyafeng.itchat4j.service.impl.LoginServiceImpl;
import cn.zhouyafeng.itchat4j.thread.CheckLoginStatusThread;
import cn.zhouyafeng.itchat4j.utils.SleepUtils;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.page.TableSupport;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.common.CommonController;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.zhong.chatgpt.wechat.bot.model.Bot;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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

//    @GetMapping("/getLoginQrCode")
//    public ResponseEntity<byte[]> getQrCode() throws InterruptedException, IOException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bot.buildMyAiWehatBot().start();
//            }
//        }).start();
//
//        File file = new File(qrCode + "/QR.jpg");
//        // 检测登录qrcode是否生成，只检测10次
//        for (int i = 0; i < 10; i++) {
//            if(file.exists()){
//                break;
//            }
//            Thread.sleep(1000);
//            if(i == 9){
//                return null;
//            }
//        }
//
//        byte[] imageByte = Files.readAllBytes(file.toPath());
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
//        httpHeaders.setContentLength(imageByte.length);
//        file.delete();
//        return new ResponseEntity<>(imageByte,httpHeaders, HttpStatus.OK);
//    }

    @GetMapping("/getLoginQrCode")
    public void login( HttpServletResponse response) {
        Core core = new Core();
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
        new Thread(new CheckLoginStatusThread()).start();
    }
}
