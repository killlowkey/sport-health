package com.xzc.sport.health.modules.limit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Ray
 * @date created in 2020/9/2 7:45
 */
@Data
@AllArgsConstructor
public class PathInfo {

    /**
     * bean 名称
     */
    private String controllerName;

    /**
     * url
     */
    private String path;

    /**
     * 请求方法类型：get、post、delete、put....
     */
    private MethodType methodType;

    /**
     * 执行器的方法
     */
    @JsonIgnore
    private Method method;

    /**
     * 方法签名
     */
    private String methodSignature;

}
