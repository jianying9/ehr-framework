package com.ehr.framework.excel;

import com.ehr.framework.util.StringUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * EXCEL写入类
 * 
 * @author chancelin
 */
public class ExcelWriter extends ExcelSkelton {

    /**
     * 构造函数
     */
    public ExcelWriter() {
        super();
    }

    /**
     * 构造函数，从源文件路径中读取内容
     * 
     * @param filePath 源文件路径
     * @throws IOException
     */
    public ExcelWriter(String filePath) throws IOException {
        super(filePath);
    }

    /**
     * 保存内容到构造函数中提供的文件中，如果未提供，则忽略
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        if (filePath != null) {
            save(filePath);
        }
    }

    /**
     * 保存EXCEL到指定的文件中
     * 
     * @param path 要保存到的文件路径
     * @throws IOException
     */
    public void save(String path) throws IOException {
        int idx = path.lastIndexOf(System.getProperty("file.separator"));
        if (idx > 0) {
            String filePaths = path.substring(0, idx);
            // 如果路径不存在，创建路径
            File file = new File(filePaths);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        FileOutputStream fileOut = new FileOutputStream(path);
        workbook.write(fileOut);
        fileOut.close();
    }

    /**
     * 在输出流中导出excel
     * 
     * @param os 输出流
     * @throws IOException
     */
    public void write(OutputStream os) throws IOException {
        workbook.write(os);
    }

    /**
     * 添加指定的二维数组内容到一个新的工作区中
     * 
     * @param data 二维数组
     * @return 导入到的工作区序号
     */
    public int addSheet(String[][] data) {
        return addSheet(null, data, null, 0);
    }

    /**
     * 添加内容到一个新的工作区中
     * 
     * @param data 二维数组
     * @return 导入到的工作区序号
     */
    public int addSheet(List<String[]> data) {
        return addSheet(null, data, null, 0);
    }

    public int addSheet(String sheetName, String[][] data) {
        return addSheet(sheetName, data, null, 0);
    }

    public int addSheet(String sheetName, List<String[]> data) {
        return addSheet(sheetName, data, null, 0);
    }

    /**
     * 根据条件，生成工作薄对象到内存super
     * 
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param data 数据
     * @return sheet order
     */
    public int addSheet(String sheetName, String[][] data, String[] headers) {
        return addSheet(sheetName, data, headers, 0);
    }

    public int addSheet(String sheetName, List<String[]> data, String[] headers) {
        return addSheet(sheetName, data.toArray(new String[][]{}), headers, 0);
    }

    public int addSheet(String[][] data, int startRow) {
        return addSheet(null, data, null, startRow);
    }

    public int addSheet(List<String[]> data, int startRow) {
        return addSheet(null, data, null, startRow);
    }

    public int addSheet(String sheetName, String[][] data, int startRow) {
        return addSheet(sheetName, data, null, startRow);
    }

    public int addSheet(String sheetName, List<String[]> data, int startRow) {
        return addSheet(sheetName, data, null, startRow);
    }

    /**
     * 根据条件，生成工作薄对象到内存
     * 
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param data 数据
     * @return sheet order
     */
    public int addSheet(String sheetName, String[][] data, String[] headers, int startRow) {
        // 产生工作表对象
        Sheet sheet = null;
        if (sheetName != null && sheetName.length() > 0) {
            sheet = workbook.getSheet(sheetName);
        }
        if (sheet == null) {
            sheet = (sheetName == null || sheetName.length() == 0) ? workbook.createSheet() : workbook.createSheet(sheetName);
        }
        // 产生一行
        Row row;
        // 产生单元格
        Cell cell;
        if (headers != null) {
            row = getRow(sheet, startRow, true);
            // 写入各个字段的名称
            for (int iHeader = 0; iHeader < headers.length; iHeader++) {
                // 创建第一行各个字段名称的单元格
                cell = getCell(row, iHeader, true);
                // 设置单元格内容为字符串型
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // 给单元格内容赋值
                cell.setCellValue(new HSSFRichTextString(headers[iHeader]));
            }
        }
        // 写入各条记录,每条记录对应excel表中的一行
        if (data != null) {
            for (int iRow = 0; iRow < data.length; iRow++) {
                String[] oneRow = data[iRow];
                // 生成一行
                row = getRow(sheet, startRow + iRow + (headers != null ? 1 : 0), true);
                for (int iCol = 0; oneRow != null && iCol < oneRow.length; iCol++) {
                    cell = getCell(row, iCol, true);
                    // 设置单元格字符类型为String
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell.setCellValue(new HSSFRichTextString(StringUtils.nullValue(oneRow[iCol])));
                }
            }
        }
        return workbook.getSheetIndex(sheetName);
    }

    public int addSheet(String sheetName, List<String[]> data, String[] headers, int startRow) {
        return addSheet(sheetName, data.toArray(new String[][]{}), headers, startRow);
    }

    public void setRow(int sheetOrder, int row, String[] rowDatas) {
        setRow(getRow(sheetOrder, row, true), 0, rowDatas);
    }

    public void setRow(int sheetOrder, int row, int startCol, String[] rowDatas) {
        setRow(getRow(sheetOrder, row, true), startCol, rowDatas);
    }

    public void setRow(Sheet sheet, int row, String[] rowDatas) {
        setRow(getRow(sheet, row, true), 0, rowDatas);
    }

    public void setRow(Sheet sheet, int row, int startCol, String[] rowDatas) {
        setRow(getRow(sheet, row, true), startCol, rowDatas);
    }

    public void setRow(Row row, String[] rowDatas) {
        setRow(row, 0, rowDatas);
    }

    public void setRow(Row row, int startCol, String[] rowDatas) {
        Cell cell;
        for (int iCol = startCol; rowDatas != null && iCol < rowDatas.length + startCol; iCol++) {
            cell = getCell(row, iCol, true);
            // 设置单元格字符类型为String
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(new HSSFRichTextString(StringUtils.nullValue(rowDatas[iCol - startCol])));
        }
    }

    public void setContent(int row, int column, boolean content) {
        setContent(0, row, column, content);
    }

    public void setContent(int row, int column, Date content) {
        setContent(0, row, column, content);
    }

    public void setContent(int row, int column, double content) {
        setContent(0, row, column, content);
    }

    public void setContent(int row, int column, String content) {
        setContent(0, row, column, content);
    }

    public void setContent(int sheetOrder, int row, int column, boolean content) {
        Cell cell = getCell(sheetOrder, row, column, true);
        cell.setCellValue(content);
    }

    public void setContent(int sheetOrder, int row, int column, Date content) {
        Cell cell = getCell(sheetOrder, row, column, true);
        // 设定单元格日期显示格式
        setCellFormat(cell, "yyyy-mm-dd hh:MM:ss");
        cell.setCellValue(content);
    }

    public void setContent(int sheetOrder, int row, int column, double content) {
        Cell cell = getCell(sheetOrder, row, column, true);
        cell.setCellValue(content);
    }

    public void setContent(int sheetOrder, int row, int column, String content) {
        Cell cell = getCell(sheetOrder, row, column, true);
        cell.setCellValue(content);
    }

    public int mergeCell(int firstRow, int lastRow, int firstCol, int lastCol) {
        return getSheet(0, true).addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    public int mergeCell(int sheetOrder, int firstRow, int lastRow, int firstCol, int lastCol) {
        return getSheet(sheetOrder, true).addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }

    public void setColumnFormat(int column, String format) {
        setColumnFormat(0, column, format);
    }

    public void setColumnFormat(int sheetOrder, int column, String format) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataformat.getFormat(format));
        Sheet sheet = getSheet(sheetOrder, true);
        for (int rowId = sheet.getFirstRowNum(); rowId < sheet.getLastRowNum(); rowId++) {
            setCellFormat(sheetOrder, rowId, column, format);
        }
        sheet.setDefaultColumnStyle(column, cellStyle);
    }

    public void setCellFormat(int row, int column, String format) {
        setCellFormat(0, row, column, format);
    }

    public void setCellFormat(int sheetOrder, int row, int column, String format) {
        Cell cell = getCell(sheetOrder, row, column, false);
        if (cell != null) {
            setCellFormat(cell, format);
        }
    }
}
