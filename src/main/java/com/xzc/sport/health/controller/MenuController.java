package com.xzc.sport.health.controller;

import com.xzc.sport.health.domain.MainMenu;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.log.Log;
import com.xzc.sport.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/3 13:59
 */
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/menus")
    @Log("查询菜单")
    public ResponseResult getMenu() {
        List<MainMenu> allMenu = menuService.getAllMenu();
        return ResponseResult.success(allMenu);
    }
}
