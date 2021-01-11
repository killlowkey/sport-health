package com.xzc.sport.health.config.security;

import com.xzc.sport.health.config.GlobalEnum;
import com.xzc.sport.health.domain.ResponseResult;
import com.xzc.sport.health.modules.limit.LimitFilter;
import com.xzc.sport.health.util.JwtUtils;
import com.xzc.sport.health.util.WebUtils;
import io.jsonwebtoken.lang.Maps;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Spring Security 配置
 *
 * @author Ray
 * @date created in 2020/12/12 10:23
 */
//@EnableWebSecurity(debug = true)
@Configuration
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final CorsFilter corsFilter;
    private final LimitFilter limitFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    /**
     * TODO 取消角色默认前缀
     * SecurityExpressionRoot#hasAnyRole(java.lang.String...)
     * https://blog.csdn.net/pyycsd/article/details/102803229
     *
     * @return
     */
//    @Bean
//    GrantedAuthorityDefaults grantedAuthorityDefaults() {
//        // Remove the ROLE_ prefix
//        return new GrantedAuthorityDefaults("");
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling(configurer -> {
                    configurer
                            .accessDeniedHandler(accessDeniedHandler())
                            .authenticationEntryPoint(authenticationEntryPoint());
                })
                .authorizeRequests(req -> req
                        .antMatchers("/druid/**").permitAll()
                        .antMatchers("/api/authorize/login").permitAll()
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .antMatchers("/api/limit/**", "/api/limits").hasRole("ADMIN")
                        .antMatchers("/api/log/**", "/api/logs/**", "/api/logs").hasRole("ADMIN")
                        .antMatchers("/api/users", "/api/users/**", "/api/user/**", "/api/user").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 添加跨域过滤器
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                // token 过滤器
                .addFilterBefore(new TokenFilter(), UsernamePasswordAuthenticationFilter.class)
                // 提取Json中的用户名与密码
                .addFilterAfter(restAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // 限流过滤器
                .addFilterAfter(limitFilter, FilterSecurityInterceptor.class)
                .headers(config -> config.frameOptions().disable());
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        // 允许跨域访问的主机
////        if (environment.acceptsProfiles(Profiles.of("dev"))) {
////            configuration.setAllowedOrigins(Collections.singletonList("*"));
////        } else {
////            configuration.setAllowedOrigins(Collections.singletonList("http://youthost"));
////        }
//
//        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Collections.singletonList("*"));
//        configuration.addExposedHeader("X-Authenticate");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    RestUsernamePasswordAuthenticationFilter restAuthenticationFilter() throws Exception {
        RestUsernamePasswordAuthenticationFilter filter = new RestUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManager());
        filter.setFilterProcessesUrl("/api/authorize/login");

        return filter;
    }

    AuthenticationSuccessHandler jsonAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 登录成功
            ResponseResult responseResult = ResponseResult.success("登录成功");
            responseResult.setData(Maps.of("token", JwtUtils.generateToken(authentication)).build());
            WebUtils.sendJsonMessage(response, responseResult);
        };
    }

    AuthenticationFailureHandler jsonAuthenticationFailureHandler() {
        return (request, response, exception) -> {
            // 登录失败
            WebUtils.sendJsonMessage(response, ResponseResult.error(GlobalEnum.USERNAME_OR_PASSWORD_ERROR));
        };
    }

    AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
//            response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            WebUtils.sendJsonMessage(response, ResponseResult.error(403, accessDeniedException.getMessage()));
        };
    }

    AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // 当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException == null ? "Unauthorized" :
//                    authException.getMessage());
            WebUtils.sendJsonMessage(response, ResponseResult.error(401, authException.getMessage()));
        };
    }
}
