package com.xzc.sport.health.modules.limit;

import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.service.LimitService;
import com.xzc.sport.health.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ray
 * @date created in 2020/12/24 10:41
 */
@Component
@RequiredArgsConstructor
public class LimitFilter extends GenericFilterBean {

    private final LimitService limitService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!limitService.access((HttpServletRequest) request)) {
            WebUtils.sendJsonMessage((HttpServletResponse) response, ResponseResult.error("触发限流接口"));
        } else {
            chain.doFilter(request, response);
        }
    }

}
