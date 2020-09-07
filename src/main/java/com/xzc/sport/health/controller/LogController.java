package com.xzc.sport.health.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.controller.dto.LogQueryDto;
import com.xzc.sport.health.controller.vo.PageVo;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.modules.role.Role;
import com.xzc.sport.health.modules.role.hasRoles;
import com.xzc.sport.health.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Ray
 * @date created in 2020/9/6 12:29
 */
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/logs")
    @hasRoles(Role.ADMIN)
    @Log("获取日志")
    public ResponseResult getLogByPage(LogQueryDto logQueryDto) {
        IPage iPage = logService.getUserListByPage(logQueryDto);
        return ResponseResult.success(new PageVo(iPage.getTotal(), iPage.getRecords()));
    }

    @GetMapping("/log/error/{id}")
    @hasRoles(Role.ADMIN)
    @Log("获取日志异常信息")
    public ResponseResult getLogException(@PathVariable("id") long id) {
        return ResponseResult.success("success", logService.getLogException(id));
    }

    @DeleteMapping("/logs")
    @hasRoles(Role.ADMIN)
    @Log("清空日志")
    public ResponseResult deleteAllLog(@RequestParam(value = "logType", defaultValue = "") String logType) {
        logService.deleteAllLimit(logType);
        return ResponseResult.success(GlobalAttributes.LOG_DELETED_SUCCESS);
    }
}
