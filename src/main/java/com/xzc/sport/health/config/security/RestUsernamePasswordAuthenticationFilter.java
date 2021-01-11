package com.xzc.sport.health.config.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ray
 * @date created in 2020/12/12 13:31
 */
class RestUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            /**
             * {
             *     "username": "user",
             *     "password": "12345678"
             * }
             */
            ServletInputStream is = request.getInputStream();
            JsonNode jsonNode = objectMapper.readTree(is);
            String username = jsonNode.get("username").textValue();
            String password = jsonNode.get("password").textValue();

            authRequest = new UsernamePasswordAuthenticationToken(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadCredentialsException("没有找到用户名或密码");
        }

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
