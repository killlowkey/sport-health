package com.xzc.sport.health.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 子菜单实体类
 *
 * @author Ray
 * @date created in 2020/9/3 12:52
 */
@Data
@TableName("sub_menu")
public class SubMenu {
    @TableId(type = IdType.AUTO)
    @JsonProperty("subId")
    private Long id;

    @JsonProperty("subTitle")
    private String title;

    @JsonProperty("subPath")
    private String path;

    @JsonProperty("subIcon")
    private String icon;

    @JsonIgnore
    private Long mid;

    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonIgnore
    private LocalDateTime updateTime;
}
