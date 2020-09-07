package com.xzc.sport.health.modules.limit;

import com.xzc.sport.health.config.RequestHolder;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ray
 * @date created in 2020/9/1 22:19
 */
public abstract class LimitHolder {

    private static final Map<String, List<PathInfo>> pathInfoMap = new ConcurrentHashMap<>();
    private static final List<Limit> limitList = new ArrayList<>();
    private static final List<String> DEFAULT_EXCLUDE_CONTROLLER_NAME = Arrays.asList("org.springframework.boot" +
            ".autoconfigure.web.servlet.error.BasicErrorController");
    private static final List<String> excludeControllerName = new ArrayList<>();


    static {
        excludeControllerName.addAll(DEFAULT_EXCLUDE_CONTROLLER_NAME);
    }

    /**
     * 添加排除名单
     *
     * @param controllerName：Controller bean 名称
     */
    public static void addExcludeControllerName(String controllerName) {
        excludeControllerName.add(controllerName);
    }

    public static void addPathInfo(String controllerName, PathInfo pathInfo) {
        if (excludeControllerName.contains(controllerName)) return;

        if (!pathInfoMap.containsKey(controllerName)) {
            pathInfoMap.put(controllerName, new ArrayList<>());
        }

        pathInfoMap.putIfAbsent(controllerName, new ArrayList<>()).add(pathInfo);
    }

    public static void addAllPathInfo(String controllerName, List<PathInfo> pathInfos) {
        pathInfos.forEach(pathInfo -> addPathInfo(controllerName, pathInfo));
    }

    public static Map<String, List<PathInfo>> getPathInfoMap() {
        return Collections.unmodifiableMap(pathInfoMap);
    }

    public static void addAllLimit(List<Limit> limits) {
        for (Limit limit : limits) {
            setLimitMethod(limit.getControllerName(), limit.getMethodSignature(), limit);
            limitList.add(limit);
        }
    }

    private static void setLimitMethod(String controllerName, String methodSignature, Limit limit) {
        List<PathInfo> pathInfos = pathInfoMap.get(controllerName);

        for (PathInfo pathInfo : pathInfos) {
            if (pathInfo.getMethodSignature().equalsIgnoreCase(methodSignature)) {
                limit.setMethod(pathInfo.getMethod());
                // 需要直接return，不然限流接口会被禁用
                return;
            }
        }

        // 没有对应的控制器，然后禁用该限流接口
        // 该操作不会同步到数据库中
//        limit.setState(false);
    }

    public static PathInfo getPathInfo(Method method) {
        for (Map.Entry<String, List<PathInfo>> entry : pathInfoMap.entrySet()) {
            List<PathInfo> pathInfos = entry.getValue();
            for (PathInfo pathInfo : pathInfos) {
                if (pathInfo.getMethod().equals(method)) {
                    return pathInfo;
                }
            }
        }
        return null;
    }

    /**
     * 获取接口的key
     * 1、方法限流：bean名称+"#"+方法签名
     * 2、ip限流：bean名称+"#"+方法签名+"#"+ip地址
     *
     * @param limit
     * @return
     */
    public static String getLimitKey(Limit limit) {
        if (limit.getMethod() == null) {
            setLimitMethod(limit.getControllerName(), limit.getMethodSignature(), limit);
        }
        PathInfo pathInfo = getPathInfo(limit.getMethod());
        StringBuilder sb = new StringBuilder();
        sb.append(pathInfo.getControllerName()).append("#").append(pathInfo.getMethodSignature());

        if (limit.getLimitType() == LimitType.IP) {
            sb.append("#").append(StringUtils.getRequestIp());
        }

        return sb.toString();
    }

    public static boolean test(Method method) {
        for (Limit limit : limitList) {
            if (limit.isState() && method.equals(limit.getMethod())) {
                return true;
            }
        }

        return false;
    }


    public static Limit getLimit(Method method) {
        for (Limit limit : limitList) {
            if (limit.getMethod().equals(method)) {
                return limit;
            }
        }

        return null;
    }

    public static void addLimit(Limit limit) {
        setLimitMethod(limit.getControllerName(), limit.getMethodSignature(), limit);
        limitList.add(limit);
    }

    public static void removeLimit(Limit limit) {
        limitList.remove(limit);
    }
}
