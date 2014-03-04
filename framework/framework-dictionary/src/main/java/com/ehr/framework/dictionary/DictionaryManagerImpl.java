package com.ehr.framework.dictionary;

import com.ehr.framework.dao.Entity;
import com.ehr.framework.dao.EntityDao;
import com.ehr.framework.dao.EntityDaoContextBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author zoe
 */
public final class DictionaryManagerImpl<T extends Entity> implements DictionaryManager {

    private final Map<String, Map<String, String>> dictionaryMap;

    public DictionaryManagerImpl(final EntityDaoContextBuilder<T> entityDaoContextBuilder) {
        //获取字典
        final EntityDao dicDao = entityDaoContextBuilder.getEntityDao("Dictionary");
        final List<DictionaryEntity> dicEntityList = dicDao.inquireByColumn("inUse", "1");        //获取元素
        final EntityDao eleDao = entityDaoContextBuilder.getEntityDao("Element");
        final List<ElementEntity> eleEntityList = eleDao.inquireByColumn("inUse", "1");
        //生成数据字典
        this.dictionaryMap = new HashMap<String, Map<String, String>>(dicEntityList.size(), 1);
        Map<String, String> dicMap;
        for (DictionaryEntity dictionaryEntity : dicEntityList) {
            dicMap = new HashMap<String, String>(2, 1);
            for (ElementEntity elementEntity : eleEntityList) {
                if (dictionaryEntity.getDicId() == elementEntity.getDicId()) {
                    dicMap.put(Integer.toString(elementEntity.getEleValue()), elementEntity.getEleName());
                }
            }
            this.dictionaryMap.put(dictionaryEntity.getDicName(), dicMap);
        }
    }

    @Override
    public Map<String, String> getDictionaryMap(final String dicName) {
        return this.dictionaryMap.get(dicName);
    }

    @Override
    public String getDictionaryValue(final String dicName, final int eleValue) {
        return this.getDictionaryValue(dicName, Integer.toString(eleValue));
    }

    @Override
    public String getDictionaryValue(final String dicName, final String eleValue) {
        String eleName = "";
        if (!eleValue.isEmpty() && !dicName.isEmpty()) {
            Map<String, String> dicMap = this.dictionaryMap.get(dicName);
            if (dicMap != null) {
                eleName = dicMap.get(eleValue);
                if (eleName == null) {
                    eleName = "";
                }
            }
        }
        return eleName;
    }

    @Override
    public String getMultiDictionaryValue(final String dicName, final String eleValues) {
        String eleNames = "";
        if (!eleValues.isEmpty() && !dicName.isEmpty()) {
            String[] keys = eleValues.split(",");
            if (keys.length == 1) {
                eleNames = getDictionaryValue(dicName, keys[0]);
            } else {
                Map<String, String> dicMap = this.dictionaryMap.get(dicName);
                if (dicMap != null) {
                    boolean isExist = false;
                    String eleName;
                    StringBuilder dicBuilder = new StringBuilder(keys.length * 8);
                    for (String key : keys) {
                        eleName = dicMap.get(key);
                        if (eleName != null && !eleName.isEmpty()) {
                            dicBuilder.append(eleName).append(',');
                            isExist = true;
                        }
                    }
                    if (isExist) {
                        dicBuilder.setLength(dicBuilder.length() - 1);
                    }
                    eleNames = dicBuilder.toString();
                }
            }
        }
        return eleNames;
    }

    @Override
    public String getDictionaryKeyValue(final String dicName, final String eleName) {
        String eleValue = "-1";
        if (!eleName.isEmpty() && !dicName.isEmpty()) {
            Map<String, String> dicMap = this.dictionaryMap.get(dicName);
            if (dicMap != null) {
                Set<Entry<String, String>> set = dicMap.entrySet();
                for (Entry<String, String> entry : set) {
                    if (entry.getValue().equals(eleName)) {
                        eleValue = entry.getKey();
                        break;
                    }
                }
            }
        }
        return eleValue;
    }

    @Override
    public String getMultiDictionaryKeyValue(final String dicName, final String eleNames) {
        String eleValues = "-1";
        if (!eleNames.isEmpty() && !dicName.isEmpty()) {
            String key;
            String[] eleNameArr = eleNames.split(",");
            if (eleNameArr.length == 1) {
                String eleName = eleNameArr[0];
                eleValues = this.getDictionaryKeyValue(dicName, eleName);
            } else {
                Map<String, String> dicMap = this.dictionaryMap.get(dicName);
                if (dicMap != null) {
                    StringBuilder dicBuilder = new StringBuilder(eleNameArr.length * 3);
                    Set<Entry<String, String>> set = dicMap.entrySet();
                    String eleValue;
                    boolean isExist = false;
                    for (String eleName : eleNameArr) {
                        for (Entry<String, String> entry : set) {
                            if (entry.getValue().equals(eleName)) {
                                eleValue = entry.getKey();
                                break;
                            }
                        }
                    }
                    if (isExist) {
                        dicBuilder.setLength(dicBuilder.length() - 1);
                    }
                    eleValues = dicBuilder.toString();
                }
            }
        }
        return eleValues;
    }
}
