package com.xzc.sport.health.controller.dto;

import com.xzc.sport.health.modules.limit.LimitType;
import com.xzc.sport.health.modules.limit.MethodType;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Ray
 * @date created in 2020/9/5 17:32
 */
@Data
public class LimitDto {

    private Long id;

    @NotNull(message = "控制器名称不能为空")
    private String controllerName;

    @NotNull(message = "http路径不能为空")
    private String path;

    /**
     * 请求器方法类型
     */
    private MethodType methodType;

    /**
     * 限制类型
     */
    private LimitType limitType;

    /**
     * 方法签名
     */
    private String methodSignature;

    /**
     * true：启用
     * false：禁用
     */
    private boolean state;

    /**
     * 单位秒
     */
    @Min(value = 1, message = "间隔必须要大于0")
    private int period;

    /**
     * 最大访问次数
     */
    @Min(value = 1, message = "限制次数不能比0小")
    private int count;
}
