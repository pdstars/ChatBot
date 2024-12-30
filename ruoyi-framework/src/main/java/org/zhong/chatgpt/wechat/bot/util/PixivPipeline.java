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
import net.nekomura.utils.jixiv.utils.PixivUrlBuilder;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
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

    public String getTop(){
        int page = 1;  //頁碼
        PixivRankMode mode = PixivRankMode.DAILY;  //排行榜類別
        PixivRankContent content = PixivRankContent.ILLUST;  //作品形式
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36";  //你的User-Agent
        Jixiv.loginByCookie(this.phpSession);
        Jixiv.setUserAgent(userAgent);
        if(ISR18.equals("1")){
            mode = PixivRankMode.DAILY_R18;
        }
        try{
            Rank rank = PicUtil.rank(page,mode,content);
            int[] arrays = rank.getIds();
            StringBuilder result = new StringBuilder("=====今日TOP=====\n");
            for(int i: arrays){
                result.append(i).append("\n");
            }
            return result.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "404 - 什么都没找到";
    }

    public static void main(String[] args) throws IOException {
        System.out.println(0.1 + 0.5);
    }

    public static Rank rank(int page, PixivRankMode mode, PixivRankContent content) throws IOException {

        SocketAddress sa = new InetSocketAddress("localhost", 7890);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.HTTP,sa))
                .build();
        PixivUrlBuilder pub = new PixivUrlBuilder();
        String url;
        if (content.equals(PixivRankContent.OVERALL)) {
            pub.setPath("ranking.php");
            pub.addParameter("mode", mode);
            pub.addParameter("p", page);
            pub.addParameter("format", "json");
            url = pub.build();
        } else {
            pub.setPath("ranking.php");
            pub.addParameter("mode", mode);
            pub.addParameter("content", content);
            pub.addParameter("p", page);
            pub.addParameter("format", "json");
            url = pub.build();
        }

        Request.Builder rb = (new Request.Builder()).url(url);
        rb.addHeader("Referer", "https://www.pixiv.net");
        rb.addHeader("cookie", "PHPSESSID=" + Jixiv.PHPSESSID);
        rb.addHeader("user-agent", Jixiv.userAgent());
        rb.method("GET", (RequestBody)null);
        Response res = okHttpClient.newCall(rb.build()).execute();
        String json = ((ResponseBody) Objects.requireNonNull(res.body())).string();
        res.close();
        return new Rank(page, mode, content,null, json);
    }


}
