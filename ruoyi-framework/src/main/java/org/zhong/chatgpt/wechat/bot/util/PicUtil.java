package org.zhong.chatgpt.wechat.bot.util;

import net.nekomura.utils.jixiv.Jixiv;
import net.nekomura.utils.jixiv.Rank;
import net.nekomura.utils.jixiv.enums.rank.PixivRankContent;
import net.nekomura.utils.jixiv.enums.rank.PixivRankMode;
import net.nekomura.utils.jixiv.utils.PixivUrlBuilder;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

//获取图片
public class PicUtil {

    static String url = "https://pic.sogou.com/pics?query=%s";
    /**
     * 搜索图片
     * @param queryStr
     * @return
     */
    public static String queryPic(String queryStr){
        return null;

    }

    //获取当日涩图
    public static Rank rank(int page, PixivRankMode mode, PixivRankContent content) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
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
        return new Rank(page, mode, content, (String)null, json);
    }

}
