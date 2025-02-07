package com.ruoyi.web.controller.layui;

import cn.hutool.Hutool;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.layim.domain.ChatUser;
import com.ruoyi.layim.domain.Friend;
import com.ruoyi.layim.domain.vo.UserConfigVo;
import com.ruoyi.layim.service.ChatUserService;
import com.ruoyi.layim.service.FriendService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import org.apache.poi.util.TempFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/layim")
public class LayimChatController {
    @Autowired
    ChatUserService chatUserService;

    @Autowired
    FriendService friendService;

    @Autowired
    SysConfigServiceImpl configService;
    @RequestMapping("userInfo")
    @ResponseBody
    public R userInfo(Long userId){
        UserConfigVo userConfigVo = chatUserService.getUserConfig(userId);
        return R.ok(userConfigVo);
    }

    @RequestMapping("makeFriend")
    @ResponseBody
    public R makeFriend(@RequestBody Friend friend){
        friendService.addFriend(friend);
        return R.ok();
    }

    @RequestMapping("uploadImage")
    @ResponseBody
    public R uploadImage(@RequestParam("file") MultipartFile file){
        if (file.isEmpty()) {
            return R.fail("无文件");
        }
        if(file.getSize() > 10485760){
            return R.fail("你传那么大的文件是想干嘛？");
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = today.format(dateTimeFormatter);
        String rootPath = configService.selectConfigByKey("image.dir") + "/" + date + "/";
        String originalFilename = file.getOriginalFilename();
        int lastIndexOfDot = originalFilename.lastIndexOf(".");
        String extension = originalFilename.substring(lastIndexOfDot + 1);
        String filename = UUID.randomUUID() + "." + extension;
        String src = "/" + date + "/" + filename;
        try{
            Path path = Paths.get(rootPath + filename);
            // 保存文件到服务器路径
            Files.write(path, file.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok(src);
    }

    @RequestMapping("getImage")
    @ResponseBody
    public ResponseEntity uploadImage(String path,String fileName){
        String filePath = configService.selectConfigByKey("image.dir") + path;
        try{
            File file = new File(filePath);
            if (file.exists()) {
                // 设置响应头信息，包括文件名和文件类型
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
                // 返回文件资源
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new FileSystemResource(file));
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
