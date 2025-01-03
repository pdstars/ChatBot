//package cn.zhouyafeng.itchat4j.controller;
//
//import cn.zhouyafeng.itchat4j.core.Core;
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.zhong.chatgpt.wechat.bot.model.Bot;
//import org.zhong.chatgpt.wechat.bot.service.MpWechatService;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//@RestController
//@RequestMapping("/api")
//public class ApiController {
//    @Value("${bot.wechat.qrcode.path}")
//    private String qrCode;
//   // private static Core core = Core.getInstance();
//    private static Logger LOG = LoggerFactory.getLogger(ApiController.class);
//
//    @Autowired
//    private MpWechatService mpWechatService;
//    @GetMapping("/getLoginQrCode")
//    public ResponseEntity<byte[]> getQrCode() throws InterruptedException, IOException {
////        if (core.isAlive()) { // 已登陆
////            LOG.info("itchat4j已登陆");
////            return null;
////        }
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
//
//    @GetMapping("/getAlive")
//    public boolean getAlive(){
////        Core core = Core.getInstance();
//        return true;
//    }
//
//    @GetMapping("/getNews")
//    public boolean getNews() throws IOException {
//        LOG.info("每日爬取公众号新闻线程启动");
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd");
//        String date = today.format(dateTimeFormatter);
//        String text = "";
//        JSONObject json = mpWechatService.getMpUrlJSON();
//        text = mpWechatService.parseMpJson(json);
//        if(text.contains(date)){
//            LOG.info("爬取到的数据为" + text);
//            mpWechatService.saveMpText(text);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//
//}
