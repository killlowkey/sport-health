package com.xzc.sport.health.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzc.sport.health.modules.limit.LimitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Ray
 * @date created in 2021/1/11 11:11
 */
@Data
@TableName("sport_limit")
@AllArgsConstructor
@NoArgsConstructor
public class Limit {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String path;

    private String method;

    private LimitType limitType;

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
}
