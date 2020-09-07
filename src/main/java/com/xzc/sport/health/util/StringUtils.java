package com.xzc.sport.health.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.useragent.Browser;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.xzc.sport.health.config.RequestHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ray
 * @date created in 2020/9/6 7:38
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {

    private static final String UNKNOWN = "unknown";
    private static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp?ip=%s&json=true";

    public static String getSimpleMethodDesc(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append("#").append(method.getName()).append("(");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            sb.append(parameterTypes[i].getSimpleName());
            if (i != parameterTypes.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    public static String getMethodParams(JoinPoint joinPoint) {
        StringBuilder params = new StringBuilder();
        params.append("{");

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            params.append(args[i]);
            if (i != args.length - 1) {
                params.append(", ");
            }
        }
        params.append("}");

        return params.toString();
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    public static String getRequestIp() {
        return getIp(RequestHolder.getHttpServletRequest());
    }

    public static String getCurrentIpAddress() {
        if (getRequestIp().equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            return "本地局域网";
        }
        String api = String.format(IP_URL, getRequestIp());
        JSON json = JSONUtil.parse(HttpUtil.get(api));
        return json.getByPath("addr").toString();
    }

    public static String getBrowser(HttpServletRequest request){
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    public static String getCurrentBrowser() {
        return getBrowser(RequestHolder.getHttpServletRequest());
    }
}
