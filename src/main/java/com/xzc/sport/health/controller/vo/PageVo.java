package com.xzc.sport.health.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/3 22:32
 */
@Data
@AllArgsConstructor
public class PageVo {
    private long total;
    private List entity;
}
