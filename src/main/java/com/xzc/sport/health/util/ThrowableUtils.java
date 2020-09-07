package com.xzc.sport.health.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Ray
 * @date created in 2020/9/6 11:07
 */
public abstract class ThrowableUtils {
    /**
     * 获取堆栈信息
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}

