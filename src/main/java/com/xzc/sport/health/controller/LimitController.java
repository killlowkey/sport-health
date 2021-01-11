package com.xzc.sport.health.controller;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.service.LimitService;
import com.xzc.sport.health.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ray
 * @date created in 2021/1/11 14:06
 */
@RestController
@RequiredArgsConstructor
public class LimitController {

    private final LimitService limitService;
    private List<String> cachePathInfo;

    @GetMapping("/limits")
    @Log("获取所有限流接口")
    public ResponseResult getAllLimit() {
        return ResponseResult.success(limitService.getAllLimit());
    }

    @GetMapping("/limit/{id}")
    @Log("获取指定限流接口详情")
    public ResponseResult getLimitById(@PathVariable long id) {
        return ResponseResult.success(limitService.getLimitById(id));
    }

    @DeleteMapping("/limit/{id}")
    @Log("删除指定限流接口")
    public ResponseResult deleteLimitById(@PathVariable long id) {
        int result = limitService.deleteLimit(id);
        if (result >= 1) {
            return ResponseResult.success(GlobalAttributes.DELETE_LIMIT_SUCCESS);
        }

        return ResponseResult.error(GlobalAttributes.DELETE_LIMIT_FAILURE);
    }

    @PostMapping("/limit")
    @Log("添加限流接口")
    public ResponseResult insertLimit(@RequestBody Limit limit) {
        int result = limitService.insertLimit(limit);
        if (result >= 1) {
            return ResponseResult.success(GlobalAttributes.INSERT_LIMIT_SUCCESS);
        }

        return ResponseResult.error(GlobalAttributes.INSERT_LIMIT_FAILURE);
    }

    @PutMapping("/limit")
    @Log("更新指定限流接口")
    public ResponseResult updateLimit(@RequestBody Limit limit) {
        int result = limitService.updateLimit(limit);
        if (result >= 1) {
            return ResponseResult.success(GlobalAttributes.UPDATE_LIMIT_SUCCESS);
        }

        return ResponseResult.error(GlobalAttributes.UPDATE_LIMIT_FAILURE);
    }

    @GetMapping("/limit/pathInfo")
    @Log("获取所有请求路径")
    public ResponseResult getAllRequestPathInfo() {
        if (cachePathInfo == null) {
            cachePathInfo = limitService.getAllRequestInfo()
                    .stream()
                    .map(pathInfo -> pathInfo.getMethod().name() + " " + pathInfo.getPath())
                    .collect(Collectors.toList());
        }
        return ResponseResult.success(cachePathInfo);
    }

    @GetMapping("/limit/methodInfo")
    @Log("获取路径请求method")
    public ResponseResult getPathMethodInfo(@RequestParam String path) {
        String[] values = path.split(" ");
        if (values.length != 2) {
            return ResponseResult.error(GlobalAttributes.NOT_FOUND_PATH_METHOD);
        }

        Optional<String> methodOption = limitService.getAllRequestInfo()
                .stream()
                .filter(requestPathInfo -> requestPathInfo.getMethod().matches(values[0]) &&
                        requestPathInfo.getPath().equals(values[1]))
                .map(requestPathInfo -> requestPathInfo.getMethod().name())
                .findFirst();

        String method = methodOption.orElse("");
        if (StringUtils.hasText(method)) {
            return ResponseResult.success("success", method);
        } else {
            return ResponseResult.error(GlobalAttributes.NOT_FOUND_PATH_METHOD);
        }
    }
}
