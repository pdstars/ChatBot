package org.zhong.chatgpt.wechat.bot.util;

import cn.hutool.extra.spring.SpringUtil;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.impl.SysConfigServiceImpl;
import net.nekomura.utils.jixiv.Jixiv;
import net.nekomura.utils.jixiv.Pixiv;
import net.nekomura.utils.jixiv.Rank;
import net.nekomura.utils.jixiv.SearchResult;
import net.nekomura.utils.jixiv.enums.rank.PixivRankContent;
import net.nekomura.utils.jixiv.enums.rank.PixivRankMode;
import net.nekomura.utils.jixiv.enums.search.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class PixivPipeline {
    private String path;
    private String word;


    public PixivPipeline(){
        ISysConfigService configService = SpringUtil.getBean(SysConfigServiceImpl.class);
        this.path =  configService.selectConfigByKey("bot.workspace") + "/pipeline/pixiv";
    }
    //爬取
    public void process(String word) {
        this.word = word;
        if(StringUtils.isEmpty(word)){
            this.word = "default";
        }
        String phpSession = "86766754_RSXOw3MO4SJpfXnuzsYhc02olq7bjWQL"; //pixiv登入後cookie裡的PHPSESSID
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";  //你的User-Agent
        Jixiv.loginByCookie(phpSession);
        Jixiv.setUserAgent(userAgent);
        int page = 1;  //頁碼
        PixivSearchArtworkType artworkType = PixivSearchArtworkType.ILLUSTRATIONS;  //搜尋作品類別
        PixivSearchOrder order = PixivSearchOrder.NEW_TO_OLD;  //排序方式
        PixivSearchMode mode = PixivSearchMode.ALL;  //搜尋作品年齡分類

        PixivSearchStrictMode sMode = PixivSearchStrictMode.S_TAG;  //關鍵字搜尋方式
        PixivSearchType type = PixivSearchType.ILLUST;  //搜尋作品類別
        try{
            SearchResult result = Pixiv.search(this.word, page, artworkType, order, mode, sMode, type);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) throws IOException {
//        String phpSession = "86766754_RSXOw3MO4SJpfXnuzsYhc02olq7bjWQL"; //pixiv登入後cookie裡的PHPSESSID
//        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";  //你的User-Agent
//
//        Jixiv.loginByCookie(phpSession);
//        Jixiv.setUserAgent(userAgent);
//        int page = 1;  //頁碼
//        PixivRankMode mode = PixivRankMode.DAILY;  //排行榜類別
//        PixivRankContent content = PixivRankContent.ILLUST;  //作品形式
//        String date = "20201001";  //日期
//        Rank rank = Pixiv.rank(page, mode, content, date);
//    }
}
