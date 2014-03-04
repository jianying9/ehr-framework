package com.ehr.framework.response;

import com.ehr.framework.config.DefaultResponseFlagEnum;
import com.ehr.framework.config.ResponseFlagType;
import com.ehr.framework.dao.Entity;
import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.excel.ExcelWriter;
import com.ehr.framework.util.ExcelUtils;
import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.util.JsonUtils;
import com.ehr.framework.util.TimeUtils;
import com.ehr.framework.worker.workhandler.PageExtendedEntity;
import com.ehr.framework.worker.workhandler.WritePageParameterManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 负责将操作结果转换成JSON响应给前台
 * @author zoe
 */
public final class ResponseWriterImpl<T extends Entity> implements ResponseWriter<T> {

    //分页参数管理对象
    private final WritePageParameterManager pageParameterManager;
    //操作标志
    private ResponseFlagType flag = DefaultResponseFlagEnum.FAILURE;
    //当前action
    private String act = "";
    private String info = "";
    private String cacheId = "";
    private String ui = "";
    //存储临时信息
    private Map<String, String> mapData;
    private List<Map<String, String>> mapListData;
    private Map<String, String> extendedExcelColumnMap;

    public ResponseWriterImpl(final WritePageParameterManager pageParameterManager) {
        this.pageParameterManager = pageParameterManager;
    }

    @Override
    public String getUi() {
        return ui;
    }

    @Override
    public void setUi(String ui) {
        this.ui = ui;
    }

    @Override
    public String getCacheId() {
        return cacheId;
    }

    @Override
    public void setCacheId(String cacheId) {
        this.cacheId = cacheId;
    }
    
    @Override
    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String getFlag() {
        return this.flag.getFlagName();
    }

    @Override
    public void setFlag(ResponseFlagType flag) {
        this.flag = flag;
    }

    @Override
    public String getAct() {
        return act;
    }

    @Override
    public void setAct(String act) {
        this.act = act;
    }

    @Override
    public void setMapData(Map<String, String> parameterMap) {
        this.mapData = parameterMap;
        this.mapListData = null;
    }

    @Override
    public Map<String, String> getMapData() {
        return this.mapData;
    }

    @Override
    public void setMapListData(List<Map<String, String>> parameterMapList) {
        this.mapData = null;
        this.mapListData = parameterMapList;
    }

    @Override
    public List<Map<String, String>> getMapListData() {
        return this.mapListData;
    }

    @Override
    public void setEntityData(T t) {
        Map<String, String> entityMap = t.toMap();
        this.setMapData(entityMap);
    }

    @Override
    public void setEntityListData(List<T> tList) {
        List<Map<String, String>> entityMapList = new ArrayList<Map<String, String>>(tList.size());
        for (T t : tList) {
            entityMapList.add(t.toMap());
        }
        this.setMapListData(entityMapList);
    }

    @Override
    public ResponseDataStateEnum getResponseDataStateEnum() {
        ResponseDataStateEnum responseDataStateEnum = ResponseDataStateEnum.NO_DATA;
        if (this.mapData != null) {
            responseDataStateEnum = ResponseDataStateEnum.MAP_DATA;
        } else if (this.mapListData != null) {
            responseDataStateEnum = ResponseDataStateEnum.MAP_LIST_DATA;
        }
        return responseDataStateEnum;
    }

    @Override
    public void clear() {
        this.flag = DefaultResponseFlagEnum.FAILURE;
        this.cacheId = "";
        this.info = "";
        this.act = "";
        this.ui = "-1";
        this.mapData = null;
        this.mapListData = null;
        this.extendedExcelColumnMap = null;
    }

    @Override
    public void failure() {
        this.flag = DefaultResponseFlagEnum.FAILURE;
    }

    @Override
    public void success() {
        this.flag = DefaultResponseFlagEnum.SUCCESS;
    }

    @Override
    public void unLogin() {
        this.flag = DefaultResponseFlagEnum.UNLOGIN;
    }

    @Override
    public void denied() {
        this.flag = DefaultResponseFlagEnum.DENIED;
    }

    @Override
    public void timeOut() {
        this.flag = DefaultResponseFlagEnum.TIMEOUT;
    }

    @Override
    public void noFile() {
        this.flag = DefaultResponseFlagEnum.NO_FILE;
    }

    @Override
    public void invalid() {
        this.flag = DefaultResponseFlagEnum.INVALID;
    }

    @Override
    public void nodata() {
        this.flag = DefaultResponseFlagEnum.NO_DATA;
    }

    @Override
    public void writeJson(final HttpServletRequest request, final HttpServletResponse response) {
        StringBuilder jsonBuilder = new StringBuilder(64);
        jsonBuilder.append("{\"flag\":\"").append(this.flag.getFlagName());
        jsonBuilder.append("\",\"act\":\"").append(this.act);
        jsonBuilder.append("\",\"cacheId\":\"").append(this.cacheId);
        jsonBuilder.append("\",\"ui\":\"").append(this.ui);
        if (this.flag == DefaultResponseFlagEnum.INVALID) {
            jsonBuilder.append("\",\"info\":\"").append(this.info);
        }
        jsonBuilder.append("\",\"data\":[]}");
        HttpUtils.toWrite(request, response, jsonBuilder.toString());
    }

    @Override
    public void writeJson(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames) {
        StringBuilder jsonBuilder = new StringBuilder(64);
        jsonBuilder.append("{\"flag\":\"").append(this.flag.getFlagName());
        jsonBuilder.append("\",\"act\":\"").append(this.act);
        jsonBuilder.append("\",\"cacheId\":\"").append(this.cacheId);
        jsonBuilder.append("\",\"ui\":\"").append(this.ui);
        if (this.flag == DefaultResponseFlagEnum.INVALID) {
            jsonBuilder.append("\",\"info\":\"").append(this.info);
        }
        String data = "";
        if (fieldNames.length > 0) {
            if (this.mapData != null) {
                data = JsonUtils.mapToJSON(this.mapData, fieldNames, fieldHandlerMap);
            } else if (this.mapListData != null) {
                data = JsonUtils.mapListToJSON(this.mapListData, fieldNames, fieldHandlerMap);
            }
        }
        jsonBuilder.append("\",\"data\":[").append(data).append("]}");
        HttpUtils.toWrite(request, response, jsonBuilder.toString());
    }

    @Override
    public void writePageJson(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames) {
        StringBuilder jsonBuilder = new StringBuilder(128);
        jsonBuilder.append("{\"flag\":\"").append(this.flag.getFlagName());
        jsonBuilder.append("\",\"act\":\"").append(this.act);
        jsonBuilder.append("\",\"cacheId\":\"").append(this.cacheId);
        jsonBuilder.append("\",\"ui\":\"").append(this.ui);
        if (this.flag == DefaultResponseFlagEnum.INVALID) {
            jsonBuilder.append("\",\"info\":\"").append(this.info);
        }
        PageExtendedEntity pageExtendedEntity = this.pageParameterManager.getThreadLocal();
        jsonBuilder.append("\",\"pageIndex\":\"").append(pageExtendedEntity.getPageIndex());
        jsonBuilder.append("\",\"pageRows\":\"").append(pageExtendedEntity.getPageRows());
        jsonBuilder.append("\",\"totalRows\":\"").append(pageExtendedEntity.getTotalRows());
        jsonBuilder.append("\",\"pageNum\":\"").append(pageExtendedEntity.getPageNum());
        String data = "";
        if (fieldNames.length > 0) {
            if (this.mapData != null) {
                data = JsonUtils.mapToJSON(this.mapData, fieldNames, fieldHandlerMap);
            } else if (this.mapListData != null) {
                data = JsonUtils.mapListToJSON(this.mapListData, fieldNames, fieldHandlerMap);
            }
        }
        jsonBuilder.append("\",\"data\":[").append(data).append("]}");
        HttpUtils.toWrite(request, response, jsonBuilder.toString());
    }

    @Override
    public void writeExcel(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames, final String exportFileName) {
        StringBuilder fileNameBuilder = new StringBuilder(exportFileName.length() + 23);
        fileNameBuilder.append(exportFileName).append(TimeUtils.getDateFotmatYYMMDD()).append(".xls");
        String fileName = fileNameBuilder.toString();
        ExcelWriter excelWriter;
        if (this.mapData != null) {
            excelWriter = ExcelUtils.createExcelWriter(this.mapData, fieldNames, fieldHandlerMap);
        } else if (this.mapListData != null) {
            excelWriter = ExcelUtils.createExcelWriter(this.mapListData, fieldNames, fieldHandlerMap);
        } else {
            excelWriter = ExcelUtils.createExcelWriter(new HashMap<String, String>(2, 1), fieldNames, fieldHandlerMap);
        }
        HttpUtils.toWirteExcel(request, response, fileName, excelWriter);
    }

    @Override
    public void writeExcelModel(final HttpServletRequest request, final HttpServletResponse response, final Map<String, FieldHandler> fieldHandlerMap, final String[] fieldNames, final String exportFileName) {
        StringBuilder fileNameBuilder = new StringBuilder(exportFileName.length() + 23);
        fileNameBuilder.append(exportFileName).append(TimeUtils.getDateFotmatYYMMDD()).append(".xls");
        String fileName = fileNameBuilder.toString();
        ExcelWriter excelWriter = ExcelUtils.createExcelModelWriter(fieldNames, fieldHandlerMap, this.extendedExcelColumnMap);
        if (excelWriter != null) {
            HttpUtils.toWirteExcel(request, response, fileName, excelWriter);
        }
    }

    /**
     * 设置excel的动态扩展字段
     * @param extendedExcelColumnMap 
     */
    @Override
    public void setExtendedExcelColumn(Map<String, String> extendedExcelColumnMap) {
        this.extendedExcelColumnMap = extendedExcelColumnMap;
    }
}
