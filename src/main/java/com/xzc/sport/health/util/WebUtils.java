package com.xzc.sport.health.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.xzc.sport.health.util.JwtUtils.JWT_HEADER;
import static com.xzc.sport.health.util.JwtUtils.START_WITH;

/**
 * @author Ray
 * @date created in 2020/12/12 13:13
 */
public abstract class WebUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendJsonMessage(HttpServletResponse response,
                                       Object data) {
        // 设置一些响应的元信息
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        try {
            String jsonData = objectMapper.writeValueAsString(data);
            response.getWriter().write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getAuthorizationToken(HttpServletRequest request) {
        String header = request.getHeader(JWT_HEADER);
        if (!StringUtils.hasText(header) || !header.startsWith(START_WITH)) {
            return "";
        }

        return header.split(" ")[1];
    }

    public static String getRequestIP(HttpServletRequest request) {
       return request.getRemoteAddr();
    }
}
