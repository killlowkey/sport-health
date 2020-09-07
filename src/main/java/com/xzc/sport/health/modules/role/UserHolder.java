package com.xzc.sport.health.modules.role;

import com.xzc.sport.health.config.RequestHolder;
import com.xzc.sport.health.domain.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Ray
 * @date created in 2020/9/1 13:04
 */
public abstract class UserHolder {

    public static User getCurrentUser() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            return user;
        }

        return null;
    }

    public static String getUsername() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            return "未知用户";
        }

        return currentUser.getUsername();
    }

    public static boolean isAdmin() {
        Role role = getCurrentUser().getRole();
        return role == Role.ADMIN;
    }

    public static boolean auth(ProceedingJoinPoint joinPoint) {
        // 获取方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法注解与其参数
        hasRoles hasRoles = AnnotationUtils.findAnnotation(method, hasRoles.class);
        Role[] roles = hasRoles.value();

        // 获取当前用户的角色
        Role role = getCurrentUser().getRole();
        return container(role, roles);
    }

    private static boolean container(Role role, Role[] roles) {
        for (int i = 0; i < roles.length; i++) {
            if (roles[i] == role) {
                return true;
            }
        }

        return false;
    }
}
