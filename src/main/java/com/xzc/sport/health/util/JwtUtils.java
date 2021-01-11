package com.xzc.sport.health.util;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.ResponseResult;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ray
 * @date created in 2020/12/12 13:40
 */
public abstract class JwtUtils {

    // 默认7天过期
    private static final long EXP_DATE = 7 * 24 * 60 * 60 * 1000;
    // 盐值
    private static final Key KEY = Keys.hmacShaKeyFor("3e4b115b-3d5f-45db-8963-6735e051a753".getBytes());
    // token 存放的请求头
    public static final String JWT_HEADER = "Authorization";
    //  Basic token
    public static final String START_WITH = "Basic";

    /**
     * 根据 {@link Authentication} 生成token
     *
     * @param authentication
     * @return
     */
    public static String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Jwts
                .builder()
                .setId("sport-health")
                // 主题
                .setSubject(userDetails.getUsername())
                .claim("authorities",
                        userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                // 签发时间
                .setIssuedAt(new Date())
                // 过期时间
                .setExpiration(new Date(System.currentTimeMillis() + EXP_DATE))
                // 盐值 + 加密算法
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 根据 token 来解析为 {@link Authentication}
     *
     * @param token
     * @return
     */
    public static Authentication getAuthentication(String token, HttpServletResponse response) {
        // 1、token可能失效
        // 2、token无效或被篡改
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            List<String> authoritiesList = claims.get("authorities", List.class);
            Collection<? extends GrantedAuthority> authorities = authoritiesList
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, token, authorities);
        } catch (JwtException ex) {
            ResponseResult responseResult = ResponseResult.error();

            if (ex instanceof ExpiredJwtException) {
                // token 过期
                responseResult.setMessage(GlobalEnum.TOKEN_EXPIRED.getMessage());
            } else if (ex instanceof MalformedJwtException) {
                responseResult.setMessage(GlobalEnum.TOKEN_ERROR.getMessage());
            } else {
                responseResult.setMessage(ex.getMessage());
            }

            WebUtils.sendJsonMessage(response, responseResult);
            throw new RuntimeException(ex);
        }
    }
}
