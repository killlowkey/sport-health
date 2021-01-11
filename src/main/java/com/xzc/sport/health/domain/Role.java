package com.xzc.sport.health.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Ray
 * @date created in 2020/12/12 10:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sport_role")
public class Role {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    public Role(String name, long userId) {
        this.name = name;
        this.userId = userId;
    }
}
