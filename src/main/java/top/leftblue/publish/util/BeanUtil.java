package top.leftblue.publish.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtil implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanUtil.beanFactory = beanFactory;
    }

    public static  <T> T getBean(Class<T> clazz) {
        return beanFactory.getBean(clazz);
    }

}
