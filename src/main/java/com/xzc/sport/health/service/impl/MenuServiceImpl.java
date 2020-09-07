package com.xzc.sport.health.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xzc.sport.health.domain.MainMenu;
import com.xzc.sport.health.domain.SubMenu;
import com.xzc.sport.health.mapper.MenuMapper;
import com.xzc.sport.health.mapper.SubMenuMapper;
import com.xzc.sport.health.modules.role.Role;
import com.xzc.sport.health.modules.role.UserHolder;
import com.xzc.sport.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ray
 * @date created in 2020/9/3 12:56
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private SubMenuMapper subMenuMapper;

    @Override
    public List<MainMenu> getAllMenu() {

        // 设计并不好，该功能仅做演示
        List<MainMenu> mainMenus;
        // 获取当前登录用户的角色
        Role userRole = UserHolder.getCurrentUser().getRole();

        if (userRole == Role.USER) {
            QueryWrapper<MainMenu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("role", userRole);
            mainMenus = menuMapper.selectList(queryWrapper);
        } else {
            // 是管理员就开放所有的权限
            mainMenus = menuMapper.selectList(null);
        }

        // 实现一对多的查询
        mainMenus.forEach(mainMenu -> {
            QueryWrapper<SubMenu> wrapper = new QueryWrapper<>();
            wrapper.eq("mid", mainMenu.getId());
            List<SubMenu> subMenus = subMenuMapper.selectList(wrapper);
            mainMenu.setSubMenuList(subMenus);
        });

        return mainMenus;
    }
}
