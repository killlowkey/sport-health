package com.xzc.sport.health.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xzc.sport.health.modules.role.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 主菜单实体类
 *
 * @author Ray
 * @date created in 2020/9/3 12:49
 */
@Data
@TableName(value = "main_menu", autoResultMap = true)
public class MainMenu {
    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private long id;

    /**
     * 菜单标题
     */
    private String title;

    private String icon;

    /**
     * 菜单路径
     */
    private String path;

    @JsonIgnore
    private Role role;

    @TableField(exist = false)
    private List<SubMenu> subMenuList;

    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonIgnore
    private LocalDateTime updateTime;
}
