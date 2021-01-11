package com.xzc.sport.health.pattern;

import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

/**
 * @author Ray
 * @date created in 2020/12/18 10:48
 */
class UrlMatcherTest {

    @Test
    void matcher() {
        AntPathMatcher matcher = new AntPathMatcher("/");
        Assert.isTrue(matcher.match("/user/**", "/user/kill/key"));
//        Assert.isTrue(matcher.match("/admin/**", "/test/hello"));
    }
}
