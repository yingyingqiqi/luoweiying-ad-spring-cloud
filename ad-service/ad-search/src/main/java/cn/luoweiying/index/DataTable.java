package cn.luoweiying.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    public static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<Class,Object>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;  //ApplicationContextAware 注入
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    public static <T> T of(Class<T> clazz) {

        T instance = (T) dataTableMap.get(clazz);
        if (null != instance) {
                return instance;
        }
        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    private static <T> T bean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }
    private static <T> T bean(Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }

}
