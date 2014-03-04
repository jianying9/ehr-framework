package com.ehr.framework.cache;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zoe
 */
public class ParameterCache {

    private final Map<String, String[]> paramsMap;

    public ParameterCache(int length) {
        this.paramsMap = new HashMap<String, String[]>(length, 1);
    }
    
    public void setParameterMap(Map<String, String> parameterMap) {
        String value;
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            value = entry.getValue();
            String[] values = {value};
            this.paramsMap.put(entry.getKey(), values);
        }
    }

    public void setParameterMaps(Map<String, String[]> parameterMap) {
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            this.paramsMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void setParameter(String name, String value) {
        String[] values = {value};
        this.setParameterValues(name, values);
    }

    public void setParameterValues(String name, String[] values) {
        this.paramsMap.put(name, values);
    }
    
    public String getParameter(String name) {
        String result = null;
        if (this.paramsMap.containsKey(name)) {
            result = this.paramsMap.get(name)[0];
        }
        return result;
    }

    public String[] getParameterValues(String name) {
        String[] result = null;
        if (this.paramsMap.containsKey(name)) {
            result = this.paramsMap.get(name);
        }
        return result;
    }
}
