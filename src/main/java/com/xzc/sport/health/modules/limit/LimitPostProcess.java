package com.xzc.sport.health.modules.limit;

import com.xzc.sport.health.domain.Limit;
import com.xzc.sport.health.service.LimitService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

/**
 * 获取所有的控制器并封装成 {@link PathInfo} 对象
 *
 * @author Ray
 * @date created in 2020/9/2 7:53
 */
@Component
public class LimitPostProcess implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping mapping;

    @Autowired
    private LimitService limitService;

    @Override
    public void run(String... args) throws Exception {
        pathInfoProcess();
        List<Limit> limitList = limitService.findAllLimit();
        rejectAllLimit(limitList);
        cleanLimit(limitList);
    }

    private void rejectAllLimit(List<Limit> limitList) {
        LimitHolder.addAllLimit(limitList);
    }

    private void cleanLimit(List<Limit> limitList) {
        limitList.forEach(limit -> {
            if (limit.getMethod() == null && !limit.isState()) {
                // 防止更新原先的List中的状态
//                Limit temp = new Limit();
//                BeanUtils.copyProperties(limit, temp);
//                temp.setState(false);
                limit.setState(false);
                LimitHolder.removeLimit(limit);
                limitService.update(limit);
            }
        });
    }

    private void pathInfoProcess() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        String currentControllerName = null;

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            String beanName = handlerMethod.getBeanType().getName();

            if (currentControllerName != beanName && currentControllerName != null) {
                currentControllerName = beanName;
            }

            if (currentControllerName == null) {
                currentControllerName =  beanName;
            }

            if (handlerMethod.getBeanType().getName().equalsIgnoreCase(currentControllerName)) {
                String info = mappingInfo.toString().substring(1, mappingInfo.toString().length() - 1);
                String[] strs = info.split(" ");
                // 获取请求方法类型 "{GET /test}" => GET
                MethodType methodType = MethodType.of(strs[0]);
                // url "{GET /test}" => /test
                String path = strs[1];
                // 方法签名 "com.xzc.sport.health.controller.AuthController#get()" => get()
                String methodSignature = handlerMethod.toString().split("#")[1];
                PathInfo pathInfo = new PathInfo(currentControllerName, path, methodType, handlerMethod.getMethod(), methodSignature);
                LimitHolder.addPathInfo(currentControllerName, pathInfo);
            }
        }
    }
}
