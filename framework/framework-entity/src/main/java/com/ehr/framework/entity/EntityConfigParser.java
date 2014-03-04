package com.ehr.framework.entity;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldConfig;
import com.ehr.framework.entity.field.FieldContextBuilder;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.entity.field.FieldHandlerBuilder;
import com.ehr.framework.logger.LogFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

/**
 * 负责解析annotation EntityConfig
 * @author zoe
 */
public class EntityConfigParser<T extends Entity> {

    private final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 解析方法
     * @param <T>
     * @param clazz 
     */
    public void parse(final Class<T> clazz, final EntityContextBuilder<T> entityCtxBuilder) {
        this.logger.debug("-----------------parsing entity {}-----------------", clazz.getName());
        if (clazz.isAnnotationPresent(EntityConfig.class)) {
            //1.获取注解EntityConfig
            final EntityConfig entityConfig = clazz.getAnnotation(EntityConfig.class);
            //3.获取实体标识
            final String entityName = entityConfig.entityName();
            //5获取该实体所有字段集合
            Field[] fieldTemp = clazz.getDeclaredFields();
            List<Field> fieldList = new ArrayList<Field>(fieldTemp.length);
            int modifier;
            for (Field field : fieldTemp) {
                modifier = field.getModifiers();
                if (!Modifier.isStatic(modifier)) {
                    fieldList.add(field);
                }
            }
            Field[] fields = fieldList.toArray(new Field[fieldList.size()]);
            //11.解析FieldConfig
            FieldConfig fieldConfig;
            Map<String, FieldHandler> fieldHandlerMap = new HashMap<String, FieldHandler>(2, 1);
            FieldHandlerBuilder fieldHandlerBuilder;
            FieldHandler fieldHandler;
            final FieldContextBuilder fieldContextBuilder = entityCtxBuilder.getFieldContextBuilder();
            for (Field field : fields) {
                if (field.isAnnotationPresent(FieldConfig.class)) {
                    fieldConfig = field.getAnnotation(FieldConfig.class);
                    fieldHandlerBuilder = new FieldHandlerBuilder(fieldContextBuilder, field.getName(), fieldConfig);
                    fieldHandler = fieldHandlerBuilder.build();
                    fieldHandlerMap.put(field.getName(), fieldHandler);
                }
            }
            //12.保存EntityHandler
            EntityHandler<T> entityHandler = new EntityHandlerImpl(fieldHandlerMap);
            entityCtxBuilder.putEntityHandler(entityName, entityHandler, clazz.getName());
            this.logger.debug("-----------------parse entity {} finished-----------------", clazz.getName());
        } else {
            this.logger.error("-----------------parse entity {} missing annotation EntityConfig-----------------", clazz.getName());
        }
    }
}
