package com.xzc.sport.health.modules.limit;

import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.util.WebUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * 路径匹配器
 *
 * @author Ray
 * @date created in 2021/1/11 10:47
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
public class LimitMatcher {

    private static final AntPathMatcher MATCHER = new AntPathMatcher("/");
    private String path;
    private HttpMethod method;
    private Limit limit;

    public LimitMatcher(Limit limit) {
        this.limit = limit;
        this.method = HttpMethod.valueOf(limit.getMethod());
        this.path = limit.getPath();
    }

    public LimitMatcher(String path, String method) {
        this.method = HttpMethod.valueOf(method);
        this.path = path;
    }

    public boolean match(HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return MATCHER.match(path, requestPath) && method.matches(request.getMethod());
    }

    public String generateKey(HttpServletRequest request) {
        String pathInfo = request.getRequestURI();
        if (limit.getLimitType() == LimitType.PATH) {
            return pathInfo;
        } else {
            String requestIP = WebUtils.getRequestIP(request);
            return pathInfo + "#" + requestIP;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LimitMatcher) {
            LimitMatcher limitMatcher = (LimitMatcher) obj;
            return path.equals(limitMatcher.path) && method.matches(limitMatcher.getMethod().name());
        }

        return false;
    }
}
