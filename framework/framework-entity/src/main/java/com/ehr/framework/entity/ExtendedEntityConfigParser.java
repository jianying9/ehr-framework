package com.ehr.framework.entity;

import com.ehr.framework.entity.field.FieldConfig;
import com.ehr.framework.entity.field.FieldContextBuilder;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.entity.field.FieldHandlerBuilder;
import com.ehr.framework.logger.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

/**
 * 负责解析annotation ExtendedEntityConfig
 * @author zoe
 */
public class ExtendedEntityConfigParser {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param <?>
     * @param clazz 
     */
    public void parse(final Class<?> clazz, final ExtendedEntityContextBuilder extendedEntityCtxBuilder) {
        this.logger.debug("-----------------parsing extended entity {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(ExtendedEntityConfig.class)) {
            //1.获取注解ExtendedEntityConfig
            final ExtendedEntityConfig extendedEntityConfig = clazz.getAnnotation(ExtendedEntityConfig.class);
            //2.获取扩展实体标识
            String extendedEntityName = extendedEntityConfig.extendedEntityName();
            //3.获取该实体所有字段集合
            final Field[] fields = clazz.getDeclaredFields();
            FieldConfig fieldConfig;
            final Map<String, FieldHandler> fieldHandlerMap = new HashMap<String, FieldHandler>(2, 1);
            FieldHandlerBuilder fieldHandlerBuilder;
            FieldHandler fieldHandler;
            final FieldContextBuilder fieldContextBuilder = extendedEntityCtxBuilder.getFieldContextBuilder();
            int modifier;
            for (Field field : fields) {
                modifier = field.getModifiers();
                if (!Modifier.isStatic(modifier) && field.isAnnotationPresent(FieldConfig.class)) {
                    fieldConfig = field.getAnnotation(FieldConfig.class);
                    fieldHandlerBuilder = new FieldHandlerBuilder(fieldContextBuilder, field.getName(), fieldConfig);
                    fieldHandler = fieldHandlerBuilder.build();
                    fieldHandlerMap.put(field.getName(), fieldHandler);
                }
            }
            //4.保存ExtendedEntityHandler
            ExtendedEntityHandlerImpl extendedEntityHandler = new ExtendedEntityHandlerImpl(fieldHandlerMap);
            extendedEntityCtxBuilder.putExtendedEntityHandler(extendedEntityName, extendedEntityHandler, clazz.getName());
            this.logger.debug("-----------------parse extended entity {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse extended entity {} missing annotation ExtendedEntityConfig-----------------", clazz.getName());
        }
    }
}
