package com.ehr.framework.excel;

import com.ehr.framework.util.HttpUtils;
import com.ehr.framework.util.NumberUtils;
import com.ehr.framework.util.StringUtils;
import com.ehr.framework.util.TimeUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.InputStream;

/**
 * 封装对excel的操作，包括本地读写excel和流中输出excel<br/> 有参构造函数参数为excel的全路径<br/>
 *
 * @author chancelin
 *
 */
public class ExcelReader extends ExcelSkelton {

//    private static final DecimalFormat df = new DecimalFormat("#.####");
    /**
     * 有参构造函数
     *
     * @param filePath excel路径
     * @throws IOException
     */
    public ExcelReader(String filePath) throws IOException {
        super(filePath);
    }

    /**
     * 有参构造函数
     *
     * @param filePath excel路径
     * @throws IOException
     */
    public ExcelReader(InputStream in) throws IOException {
        super(in, false);
    }

    public ExcelReader(InputStream in, boolean isExcel2007) throws IOException {
        super(in, isExcel2007);
    }

    /**
     * 取第一个工作区，读取该工作去下的所有记录，每一条记录是一个String[]<br/> 注意如果单元格中的数据为数字将会被自动转换为字符串<br/>
     * 如果单元格中存在除数字，字符串以外的其他类型数据，将会产生错误
     *
     * @return
     */
    public List<List<String>> getDataFromSheet() {
        return getDataFromSheet(0);
    }

    /**
     * 根据工作区序号，读取该工作去下的所有记录，每一条记录是一个String[]<br/> 注意如果单元格中的数据为数字将会被自动转换为字符串<br/>
     * 如果单元格中存在除数字，字符串以外的其他类型数据，将会产生错误
     *
     * @param sheetOrder 工作区序号
     * @return
     */
    public List<List<String>> getDataFromSheet(int sheetOrder) {
        Sheet sheet = getSheet(sheetOrder, true);
        int columns = sheet.getLastRowNum() >= 0 ? sheet.getRow(0).getLastCellNum() + 1 : 0;
        List<List<String>> listData = new ArrayList<List<String>>(sheet.getLastRowNum());
        List<String> listRow;
        boolean emptyFlag;
        String value;
        for (Row row : sheet) {
            listRow = new ArrayList<String>(columns);
            // 遍历每一行
            emptyFlag = true;
            for (int cols = 0; cols < columns; cols++) {
                value = readString(row, cols);
                value = HttpUtils.trim(value);
                if (!value.isEmpty()) {
                    emptyFlag = false;
                }
                listRow.add(readString(row, cols));
            }
            if (!emptyFlag) {
                listData.add(listRow);
            }
        }
        return listData;
    }

    /**
     * 读取指定单元格的boolean类型值
     *
     * @param sheetOrder 工作区序号
     * @param row 行号
     * @param column 列号
     * @return 单元格内容
     */
    public boolean readBoolean(int sheetOrder, int row, int column) {
        Object value = read(getCell(sheetOrder, row, column, true));
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return StringUtils.booleanValue(StringUtils.nullValue(value));
    }

    /**
     * 读取指定单元格的boolean数值型值
     *
     * @param sheetOrder 工作区序号
     * @param row 行号
     * @param column 列号
     * @return 单元格内容
     */
    public double readNumeric(int sheetOrder, int row, int column) {
        Object value = read(getCell(sheetOrder, row, column, true));
        if (value instanceof Double) {
            return (Double) value;
        }
        return StringUtils.doubleValue(StringUtils.nullValue(value));
    }

    /**
     * 读取指定单元格的boolean日期型值
     *
     * @param sheetOrder 工作区序号
     * @param row 行号
     * @param column 列号
     * @return 单元格内容
     */
    public Date readDate(int sheetOrder, int row, int column) {
        Object value = read(getCell(sheetOrder, row, column, true));
        if (value instanceof Double) {
            return DateUtil.getJavaDate((Double) value);
        } else if (value instanceof Date) {
            return (Date) value;
        }
        return null;
    }

    /**
     * 读取指定单元格的boolean字符型值
     *
     * @param sheetOrder 工作区序号
     * @param row 行号
     * @param column 列号
     * @return 单元格内容
     */
    public String readString(int sheetOrder, int row, int column) {
        Object value = read(getCell(sheetOrder, row, column, true));
        return StringUtils.nullValue(value);
    }

    /**
     * 读取指定单元格的记录
     *
     * @param colum 列号
     * @param row 行号
     * @return 单元格内容
     */
    public boolean readBoolean(int row, int column) {
        return readBoolean(0, row, column);
    }

    /**
     * 读取指定单元格的记录
     *
     * @param colum 列号
     * @param row 行号
     * @return 单元格内容
     */
    public double readNumeric(int row, int column) {
        return readNumeric(0, row, column);
    }

    /**
     * 读取指定单元格的记录
     *
     * @param colum 列号
     * @param row 行号
     * @return 单元格内容
     */
    public Date readDate(int row, int column) {
        return readDate(0, row, column);
    }

    /**
     * 读取指定单元格的记录
     *
     * @param colum 列号
     * @param row 行号
     * @return 单元格内容
     */
    public String readString(int row, int column) {
        return readString(0, row, column);
    }

    public String readString(Row row, int column) {
        Object content = read(getCell(row, column, true));
        String value = "";
        if (content instanceof Number) {
            value = NumberUtils.numberDf.format(content);
        } else if (content instanceof Date) {
            Date date = (Date) content;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (calendar.get(Calendar.YEAR) <= 1900) {
                value = TimeUtils.FM_HHMMSS.format(date);
            } else if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0
                    && calendar.get(Calendar.SECOND) == 0) {
                value = TimeUtils.FM_YYMMDD.format(date);
            } else {
                value = TimeUtils.FM_YYMMDD_HHMMSS.format(date);
            }
        } else {
            value = content.toString();
        }
        return value;
    }

    /**
     * 读取指定单元格的记录
     *
     * @param colum 列号
     * @param row 行号
     * @return 单元格函数串
     */
    public String getFormula(int row, int column) {
        return getFormula(0, row, column);
    }

    /**
     * 读取指定单元格的记录
     *
     * @param sheetOrder 工作区序号
     * @param colum 列号
     * @param row 行号
     * @return 单元格内容
     */
    public String getFormula(int sheetOrder, int row, int column) {
        return getFormula(getCell(sheetOrder, row, column, true));
    }
}
