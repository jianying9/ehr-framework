package com.ehr.framework.response;

import com.ehr.framework.config.ResponseFlagType;
import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldHandler;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 负责将操作结果转换成JSON响应给前台
 *
 * @author zoe
 */
public interface ResponseWriter<T extends Entity> {

    public String getUi();

    public void setUi(String ui);

    public String getCacheId();

    public void setCacheId(String cacheId);

    public void setInfo(String info);

    public String getFlag();

    public void setFlag(ResponseFlagType flag);

    public String getAct();

    public void setAct(String act);

    public void setMapData(Map<String, String> parameterMap);

    public Map<String, String> getMapData();

    public void setMapListData(List<Map<String, String>> parameterMapList);

    public List<Map<String, String>> getMapListData();

    public void setEntityData(T t);

    public void setEntityListData(List<T> tList);

    public ResponseDataStateEnum getResponseDataStateEnum();

    public void clear();

    public void failure();

    public void success();

    public void unLogin();

    public void denied();

    public void timeOut();

    public void noFile();

    public void invalid();

    public void nodata();

    public void writeJson(final HttpServletRequest request, final HttpServletResponse response);

    public void writeJson(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames);

    public void writePageJson(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames);

    public void writeExcel(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames, final String exportFileName);

    public void writeExcelModel(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames, final String exportFileName);

    public void setExtendedExcelColumn(Map<String, String> extendedMap);
}
