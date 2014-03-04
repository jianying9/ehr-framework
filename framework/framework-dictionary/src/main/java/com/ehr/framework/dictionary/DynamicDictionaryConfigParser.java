package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.logger.LogFactory;
import org.slf4j.Logger;

/**
 * 负责解析annotation DynamicDictionaryConfig
 * @author zoe
 */
public class DynamicDictionaryConfigParser<T extends Entity, E extends DynamicDictionaryHandler<T>> {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param <?>
     * @param clazz 
     */
    public void parse(final Class<E> clazz, final DictionaryContextBuilder<T> dictionaryCtxBuilder) {
        this.logger.debug("-----------------parsing dynamic dictionary {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(DynamicDictionaryConfig.class)) {
            //1.获取注解EDynamicDictionaryConfig
            final DynamicDictionaryConfig dynamicDictionaryConfig = clazz.getAnnotation(DynamicDictionaryConfig.class);
            //2.获取动态数据字典标识
            final String dynamicDictionaryName = dynamicDictionaryConfig.dynamicDictionaryName();
            //3.生成DynamicDictionaryHandler
            DynamicDictionaryHandler<T> dynamicDictionaryHandler = null;
            try {
                dynamicDictionaryHandler = clazz.newInstance();
            } catch (Exception e) {
                this.logger.error("There was an error instancing class {}. Cause: {}", clazz.getName(), e.getMessage());
                throw new RuntimeException("There wa an error instancing class ".concat(clazz.getName()));
            }
            //重复加载报错检测
            if (dictionaryCtxBuilder.assertExistDynamicDictionary(dynamicDictionaryName)) {
                StringBuilder mesBuilder = new StringBuilder(512);
                mesBuilder.append("There was an error putting dynamicDictionaryHandler. Cause: dynamicDictionaryName reduplicated : ").append(dynamicDictionaryName);
                mesBuilder.append("\n").append("error class is ").append(clazz.getName());
                throw new RuntimeException(mesBuilder.toString());
            }
            dictionaryCtxBuilder.putDynamicDictionaryHandler(dynamicDictionaryName, dynamicDictionaryHandler);
            this.logger.debug("-----------------parse dynamic dictionary {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse dynamic dictionary {} missing annotation DynamicDictionaryConfig-----------------", clazz.getName());
        }
    }
}
