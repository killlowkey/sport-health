package com.xzc.sport.health.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/6 12:25
 */
@Data
public class LogQueryDto {
    private int pageNum;

    private int pageSize;

    private String idSort;

    private String descName;

    private String logType;

    private List<LocalDateTime> times;

}
