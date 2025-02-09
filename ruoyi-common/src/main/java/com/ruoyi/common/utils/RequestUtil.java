package com.ruoyi.common.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    //定义移动端请求的所有可能类型
    private final static String[] AGENTS_MOBILE = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" };

    public static boolean checkAgentIsMobile(){
        return checkAgentIsMobile(null);
    }

    public static boolean checkAgentIsMobile(HttpServletRequest request){
        if(request == null){
            request = getHttpServletRequest();
        }
        String ua= request.getHeader("User-Agent");
        boolean flag = false;
        if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
            // 排除 苹果桌面系统
            if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
                for (String item : AGENTS_MOBILE) {
                    if (ua.contains(item)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    public static HttpServletRequest getHttpServletRequest() {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        return request;
    }
}