package org.zhong.chatgpt.wechat.bot.util;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import net.nekomura.utils.jixiv.*;
import net.nekomura.utils.jixiv.artworks.Illustration;
import net.nekomura.utils.jixiv.enums.artwork.PixivImageSize;
import net.nekomura.utils.jixiv.enums.rank.PixivRankContent;
import net.nekomura.utils.jixiv.enums.rank.PixivRankMode;
import net.nekomura.utils.jixiv.enums.search.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class PixivPipeline {
    private String path;
    private String word;

     private String ISR18 = "0";

     private String phpSession;


    public PixivPipeline(String phpSession){
        ISysConfigService configService = SpringUtil.getBean(SysConfigServiceImpl.class);
        this.path =  configService.selectConfigByKey("bot.workspace") + "/pipeline/pixiv";
        //this.path =  "D:\\TEST\\2";
        this.phpSession = phpSession;
    }

    public void setR18(){
        this.ISR18 = "1";
    }
    //爬取
    public String process(String word) {
        this.word = word;
        if(StringUtils.isEmpty(word)){
            this.word = "default";
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";  //你的User-Agent
        Jixiv.loginByCookie(this.phpSession);
        Jixiv.setUserAgent(userAgent);
        int page = 1;  //頁碼
        PixivSearchArtworkType artworkType = PixivSearchArtworkType.ILLUSTRATIONS;  //搜尋作品類別
        PixivSearchOrder order = PixivSearchOrder.POPULAR_FEMALE;  //排序方式
        PixivSearchMode mode = PixivSearchMode.SAFE;  //搜尋作品年齡分類
        if(ISR18.equals("1")){
            mode = PixivSearchMode.R18;
        }
        PixivSearchStrictMode sMode = PixivSearchStrictMode.S_TAG;  //關鍵字搜尋方式
        PixivSearchType type = PixivSearchType.ILLUST;  //搜尋作品類別
        try{
            SearchResult result = Pixiv.search(this.word, page, artworkType, order, mode, sMode, type);
            int[] ids = result.getIds();
            int random = (int) (Math.random() * ids.length);
            IllustrationInfo iInfo = Illustration.getInfo(ids[random]);
            String foldPath = this.path + "/" + word + "/" + random;
            iInfo.downloadAll(foldPath, PixivImageSize.ORIGINAL);
            return foldPath;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 精确查询
     * @param word
     * @return
     */
    public String queryById(String word) {
        this.word = word;
        if(StringUtils.isEmpty(word)){
            this.word = "default";
        }
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";  //你的User-Agent
        Jixiv.loginByCookie(this.phpSession);
        Jixiv.setUserAgent(userAgent);
        try{
            IllustrationInfo iInfo = Illustration.getInfo(Integer.parseInt(word));
            String foldPath = this.path + "/" + word;
            iInfo.downloadAll(foldPath, PixivImageSize.ORIGINAL);
            return foldPath;
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String getTop(String text){
        int page = 1;  //頁碼
        PixivRankMode mode = PixivRankMode.DAILY;  //排行榜類別
        PixivRankContent content = PixivRankContent.ILLUST;  //作品形式
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = today.format(formatter);
        try{
            Rank rank = Pixiv.rank(page, mode, content, formattedDate);
            int id = rank.getInfo(Integer.parseInt(text)).getId();
            IllustrationInfo iInfo = Illustration.getInfo(id);
            String foldPath = this.path + "/" + formattedDate + "/" + text;
            iInfo.downloadAll(foldPath, PixivImageSize.ORIGINAL);
            return foldPath;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) throws IOException {
        PixivPipeline p = new PixivPipeline("86766754_RSXOw3MO4SJpfXnuzsYhc02olq7bjWQL");
        p.queryById("87148678");
    }
}
