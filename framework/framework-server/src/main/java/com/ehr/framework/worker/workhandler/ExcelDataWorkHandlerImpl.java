package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.field.FieldHandler;
import com.ehr.framework.excel.ExcelReader;
import com.ehr.framework.file.UploadFileManager;
import com.ehr.framework.logger.LogFactory;
import com.ehr.framework.response.ResponseWriter;
import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.worker.HttpServletRequestManager;
import com.ehr.framework.worker.HttpServletResponseManager;
import com.ehr.framework.worker.ResponseWriterManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;

/**
 * excel导入解析excel数据，并验证
 * @author zoe
 */
public class ExcelDataWorkHandlerImpl implements WorkHandler {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final WorkHandler nextWorkHandler;
    private final String[] importantParameter;
    private final String[] minorParameter;
    private final Map<String, FieldHandler> fieldHandlerMap;
    private final UploadFileManager uploadFileManager;
    private final WriteExcelDataManager listMapManager;
    private final HttpServletRequestManager httpServletRequestManager;
    private final ResponseWriterManager responseWriterManager;
    private final HttpServletResponseManager httpServletResponseManager;

    public ExcelDataWorkHandlerImpl(final UploadFileManager uploadFileManager, final String[] importantParameter, final String[] minorParameter, final Map<String, FieldHandler> fieldHandlerMap, final WriteExcelDataManager listMapManager, final HttpServletRequestManager httpServletRequestManager, final HttpServletResponseManager httpServletResponseManager, final ResponseWriterManager responseWriterManager, final WorkHandler workHandler) {
        this.uploadFileManager = uploadFileManager;
        this.nextWorkHandler = workHandler;
        this.importantParameter = importantParameter;
        this.minorParameter = minorParameter;
        this.fieldHandlerMap = fieldHandlerMap;
        this.listMapManager = listMapManager;
        this.httpServletRequestManager = httpServletRequestManager;
        this.responseWriterManager = responseWriterManager;
        this.httpServletResponseManager = httpServletResponseManager;
    }

    private String[] getHeader() {
        String[] header = new String[this.importantParameter.length];
        String name;
        for (int index = 0; index < this.importantParameter.length; index++) {
            name = this.fieldHandlerMap.get(this.importantParameter[index]).getExportName();
            header[index] = HttpUtils.trim(name);
        }
        return header;
    }

    private String validateHeader(String[] headers, List<String> list) {
        String message = "";
        if (headers.length <= list.size()) {
            String header;
            String content;
            StringBuilder messageBuilder = new StringBuilder(64);
            for (int index = 0; index < headers.length; index++) {
                header = headers[index];
                content = list.get(index).trim();
                if (!header.equalsIgnoreCase(content)) {
                    messageBuilder.append("字段不匹配: 第").append(index + 1).append("列,要求 '").append(header).append("',实际 ").append(content).append("'\\n");
                }
            }
            if (messageBuilder.length() > 0) {
                message = messageBuilder.toString();
            }
        } else {
            StringBuilder messageBuilder = new StringBuilder(32);
            messageBuilder.append("字段数量不匹配，要求").append(headers.length).append("，实际").append(list.size());
            message = messageBuilder.toString();
        }
        return message;
    }

    @Override
    public void execute() {
        HttpServletRequest request = this.httpServletRequestManager.getThreadLocal();
        String filePath = request.getParameter("filePath");
        ResponseWriter responseWriter = this.responseWriterManager.getThreadLocal();
        String errorMsg = "";
        if (filePath == null || filePath.isEmpty()) {
            responseWriter.noFile();
        } else {
            File file = this.uploadFileManager.getFileByUrl(filePath);
            if (file == null) {
                responseWriter.noFile();
            } else {
                //获取excel数据
                List<List<String>> excelDatalist = null;
                try {
                    ExcelReader excelrd = new ExcelReader(new FileInputStream(file), filePath.endsWith(".xlsx"));
                    excelDatalist = excelrd.getDataFromSheet();
                    excelrd = null;
                } catch (IOException e) {
                    this.logger.error("reading excel data error.", e);
                }
                FieldHandler fieldHandler;
                int columnIndex;
                String parameterValue;
                if (excelDatalist != null && !excelDatalist.isEmpty()) {
                    //验证excel标题
                    String[] headers = this.getHeader();
                    //开始验证excel标题列是否正确（不包含扩展字段）
                    errorMsg = this.validateHeader(headers, excelDatalist.get(0));
                    if (errorMsg.isEmpty()) {
                        //头验证通过
                        StringBuilder messageBuilder = new StringBuilder(32);
                        for (int paraIndex = 0; paraIndex < this.importantParameter.length; paraIndex++) {
                            fieldHandler = this.fieldHandlerMap.get(this.importantParameter[paraIndex]);
                            //验证第columnIndex列的所有值
                            columnIndex = paraIndex;
                            for (int rowIndex = 1; rowIndex < excelDatalist.size(); rowIndex++) {
                                parameterValue = excelDatalist.get(rowIndex).get(columnIndex);
                                if (parameterValue == null) {
                                    messageBuilder.append("第").append(rowIndex + 1).append("行").append("第").append(columnIndex + 1).append("列不能为空");
                                    errorMsg = messageBuilder.toString();
                                    break;
                                } else {
                                    parameterValue = HttpUtils.trim(parameterValue);
                                    parameterValue = fieldHandler.getDictionaryKey(parameterValue);
                                    errorMsg = fieldHandler.validate(parameterValue);
                                    if (!errorMsg.isEmpty()) {
                                        messageBuilder.append("第").append(rowIndex + 1).append("行").append("第").append(columnIndex + 1).append("列验证失败：数据长度超出或存在非法字符");
                                        errorMsg = messageBuilder.toString();
                                        break;
                                    }
                                }
                            }
                            if (!errorMsg.isEmpty()) {
                                break;
                            }
                        }
                    }
                } else {
                    errorMsg = "NODATA";
                }
                if (errorMsg.isEmpty()) {
                    //开始取值
                    Map<String, String> parameterMap;
                    int length = this.importantParameter.length + this.minorParameter.length;
                    List<String> excelHeadDataList = excelDatalist.get(0);
                    //如果自定义头信息存在，则保存头信息
                    int extendedHeadLength = excelHeadDataList.size() - this.importantParameter.length;
                    Map<String, String> extendedHeadMap;
                    if (extendedHeadLength > 0) {
                        //存在自定义字段
                        int excelDataIndex;
                        String excelDataValue;
                        extendedHeadMap = new HashMap<String, String>(extendedHeadLength, 1);
                        for (int mirrorIndex = 0; mirrorIndex < this.minorParameter.length && mirrorIndex < extendedHeadLength; mirrorIndex++) {
                            excelDataIndex = mirrorIndex + this.importantParameter.length;
                            excelDataValue = excelHeadDataList.get(excelDataIndex);
                            excelDataValue = HttpUtils.trim(excelDataValue);
                            if (excelDataValue.isEmpty()) {
                                break;
                            } else {
                                extendedHeadMap.put(this.minorParameter[mirrorIndex], excelDataValue);
                            }
                        }
                    } else {
                        //不存在自定义字段
                        extendedHeadMap = new HashMap<String, String>(2, 1);
                    }
                    //移除excel的头信息
                    excelDatalist.remove(0);
                    //获取数据
                    List<Map<String, String>> allDataMap = new ArrayList<Map<String, String>>(excelDatalist.size());
                    String parameterName;
                    for (List<String> row : excelDatalist) {
                        parameterMap = new HashMap<String, String>(length, 1);
                        //获取固定数据，会验证数据有效性，并且自动做数据字典转换
                        for (int index = 0; index < this.importantParameter.length; index++) {
                            parameterName = this.importantParameter[index];
                            fieldHandler = this.fieldHandlerMap.get(parameterName);
                            if (row.size() > index) {
                                parameterValue = fieldHandler.getDictionaryKey(row.get(index));
                                parameterValue = HttpUtils.trim(parameterValue);
                            } else {
                                parameterValue = "";
                            }
                            parameterMap.put(parameterName, parameterValue);
                        }
                        //获取扩展数据，直接保存，不做任何处理
                        for (int index = 0; index < this.minorParameter.length && index < extendedHeadLength; index++) {
                            columnIndex = this.importantParameter.length + index;
                            parameterName = this.minorParameter[index];
                            parameterValue = row.get(columnIndex);
                            parameterValue = HttpUtils.trim(parameterValue);
                            parameterMap.put(parameterName, parameterValue);
                        }
                        allDataMap.add(parameterMap);
                    }
                    //创建excel data
                    ExcelDataImpl excelData = new ExcelDataImpl(allDataMap, extendedHeadMap, this.importantParameter);
                    this.listMapManager.openThreadLocal(excelData);
                    this.nextWorkHandler.execute();
                    this.listMapManager.closeThreadLocal();
                } else {
                    responseWriter.invalid();
                    responseWriter.setInfo(errorMsg);
                    HttpServletResponse response = this.httpServletResponseManager.getThreadLocal();
                    responseWriter.writeJson(request, response);
                }
            }
        }
    }
}
