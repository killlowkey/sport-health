package com.xzc.sport.health.config.security;

import com.xzc.sport.health.util.JwtUtils;
import com.xzc.sport.health.util.StringUtils;
import com.xzc.sport.health.util.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token 拦截
 *
 * @author Ray
 * @date created in 2020/12/12 13:35
 */
public class TokenFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = WebUtils.getAuthorizationToken(httpServletRequest);

        // 当前请求存在 token
        if (StringUtils.hasText(token)) {
            // 解析 token，成功那就设置到 SecurityContext 中
            Authentication authentication = JwtUtils.getAuthentication(token, httpServletResponse);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}
