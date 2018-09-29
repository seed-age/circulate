package com.sunnsoft.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类，保存 ApplicationContext 对象，并直接提供常用的 getBean 方法
 *
 * 如果将此类的对象注册到 Spring 容器中，则其中的 applicationContext 字段会在容器启动完毕后，自动初始化
 * 否则，需要手动调用 {@link #setApplicationContext(ApplicationContext)} 设置一个 applicationContext 对象后才能使用此类
 *
 * @author huangkaibin
 * @date 2018-09-29
 */
@Component
public class SpringUtils implements ApplicationListener<ContextRefreshedEvent> {
    private static ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationContext = event.getApplicationContext();
    }

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }
}

