package com.xzc.sport.health.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzc.sport.health.modules.limit.LimitType;
import com.xzc.sport.health.modules.limit.MethodType;
import lombok.*;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author Ray
 * @date created in 2020/9/2 12:39
 */
@TableName("sport_limit")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Limit {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String controllerName;

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
     * 执行器方法
     */
    @TableField(exist = false)
    @JsonIgnore
    private Method method;

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
    private int period;

    /**
     * 最大访问次数
     */
    private int count;

    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonIgnore
    private LocalDateTime updateTime;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Limit) {
            Limit limit = (Limit) obj;
            if (controllerName.equalsIgnoreCase(limit.getControllerName()) &&
                    methodSignature.equalsIgnoreCase(limit.getMethodSignature())) {
                return true;
            }
        }

        return false;
    }
}
