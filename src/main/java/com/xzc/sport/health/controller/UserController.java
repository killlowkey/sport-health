package com.xzc.sport.health.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzc.sport.health.config.GlobalAttributes;
import com.xzc.sport.health.controller.dto.UserDto;
import com.xzc.sport.health.controller.vo.PageVo;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.domain.User;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.modules.role.Role;
import com.xzc.sport.health.service.UserService;
import com.xzc.sport.health.util.BindingResultUtils;
import com.xzc.sport.health.modules.role.hasRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Ray
 * @date created in 2020/9/3 21:36
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @hasRoles(Role.ADMIN)
    @Log("获取用户")
    public ResponseResult getAllUser(@RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                     @RequestParam(value = "queryName", defaultValue = "") String queryName) {
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage iPage = userService.getUserListByPage(page, queryName);
        return ResponseResult.success(new PageVo(iPage.getTotal(), iPage.getRecords()));
    }


    @PutMapping("/user")
    @hasRoles(Role.ADMIN)
    @Log("更新用户")
    public ResponseResult updateUser(@RequestBody @Valid UserDto userDto,
                                     BindingResult result) throws BindException {
        BindingResultUtils.handler(result);
        userService.updateUser(userDto);
        return ResponseResult.success(GlobalAttributes.USER_UPDATED_SUCCESS);
    }

    @PostMapping("/user")
    @hasRoles(Role.ADMIN)
    @Log("添加用户")
    public ResponseResult insertUser(@RequestBody @Valid UserDto userDto,
                                     BindingResult result) throws BindException {
        BindingResultUtils.handler(result);
        userService.insertUser(userDto);
        return ResponseResult.success(GlobalAttributes.USER_INSERT_SUCCESS);
    }

    @DeleteMapping("/user/{id}")
    @hasRoles(Role.ADMIN)
    @Log("删除用户")
    public ResponseResult deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseResult.success(GlobalAttributes.USER_DELETE_SUCCESS);
    }
}