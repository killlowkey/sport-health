package com.xzc.sport.health.modules.limit;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * @author Ray
 * @date created in 2021/1/11 13:25
 */
@Data
@AllArgsConstructor
public class RequestPathInfo {
    private HttpMethod method;
    private String path;
}
