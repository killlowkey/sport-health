package com.xzc.sport.health.controller;

import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.controller.dto.LimitDto;
import com.xzc.sport.health.controller.vo.OptionVo;
import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.limit.LimitHolder;
import com.xzc.sport.health.modules.limit.PathInfo;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.modules.role.Role;
import com.xzc.sport.health.modules.role.hasRoles;
import com.xzc.sport.health.service.LimitService;
import com.xzc.sport.health.util.BindingResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ray
 * @date created in 2020/9/5 15:57
 */
@RestController
public class LimitController {

    @Autowired
    private LimitService limitService;

    @GetMapping("/limits")
    @hasRoles(Role.ADMIN)
    @Log("获取限流接口")
    public ResponseResult getAllLimit() {
        return ResponseResult.success(limitService.findAllLimit());
    }

    @GetMapping("/limit/config")
    @hasRoles(Role.ADMIN)
    @Log("获取限流配置")
    public ResponseResult getLimitConfig() {
        return ResponseResult.success(LimitHolder.getPathInfoMap());
    }

    @PutMapping("/limit")
    @hasRoles(Role.ADMIN)
    @Log("更新限流接口")
    public ResponseResult updateLimit(@RequestBody @Valid LimitDto limitDto,
                                      BindingResult result) throws BindException {
        BindingResultUtils.handler(result);
        Limit limit = new Limit();
        BeanUtils.copyProperties(limitDto, limit);
        limitService.update(limit);
        return ResponseResult.success(GlobalAttributes.LIMIT_UPDATED_SUCCESS);
    }

    @DeleteMapping("/limit/{id}")
    @hasRoles(Role.ADMIN)
    @Log("删除限流接口")
    public ResponseResult deleteLimit(@PathVariable("id") long id) {
        limitService.deleteLimitById(id);
        return ResponseResult.success(GlobalAttributes.LIMIT_INTERFACE_DELETE_SUCCESS);
    }

    @PostMapping("/limit")
    @hasRoles(Role.ADMIN)
    @Log("添加限流接口")
    public ResponseResult insertLimit(@RequestBody @Valid LimitDto limitDto,
                                      BindingResult result) throws BindException {
        BindingResultUtils.handler(result);
        Limit limit = new Limit();
        BeanUtils.copyProperties(limitDto, limit);
        limitService.insertLimit(limit);
        return ResponseResult.success(GlobalAttributes.LIMIT_INSERT_SUCCESS);
    }

    @GetMapping("/limit/controllerOption")
    public ResponseResult getControllerOption() {
        List<OptionVo> optionVoList = new ArrayList<>();
        Map<String, List<PathInfo>> pathInfoMap = LimitHolder.getPathInfoMap();
        pathInfoMap.forEach((key, value) -> {
            String[] strs = key.split("\\.");
            optionVoList.add(new OptionVo(strs[strs.length-1], key));
        });

        return ResponseResult.success(optionVoList);
    }

    @GetMapping("/limit/pathOption")
    public ResponseResult getPathOption(@RequestParam(value = "controllerName", defaultValue = "") String controllerName) {
        List<OptionVo> optionVoList = new ArrayList<>();
        LimitHolder.getPathInfoMap()
                .getOrDefault(controllerName, new ArrayList<>())
                .forEach(pathInfo -> {
                    optionVoList.add(new OptionVo(pathInfo.getPath(), pathInfo.getPath()));
                });

        return ResponseResult.success(optionVoList);
    }

    @GetMapping("/limit/methodOption")
    public ResponseResult getMethodOption(@RequestParam(value = "controllerName", defaultValue = "") String controllerName,
                                          @RequestParam(value = "path", defaultValue = "") String path) {
        List<OptionVo> optionVoList = new ArrayList<>();
        LimitHolder.getPathInfoMap()
                .getOrDefault(controllerName, new ArrayList<>())
                .forEach(pathInfo -> {
                    if (pathInfo.getPath().equalsIgnoreCase(path)) {
                        String name = pathInfo.getMethodType().getName();
                        optionVoList.add(new OptionVo(name, name));
                    }
                });

        return ResponseResult.success(optionVoList);
    }


}