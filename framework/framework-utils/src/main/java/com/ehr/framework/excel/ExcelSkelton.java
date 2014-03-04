package com.ehr.framework.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * EXCEL操作框架类，提供基本的EXCEL文件读写操作
 * @author chancelin
 * 
 */
class ExcelSkelton {

    /**
     * 工作表
     */
    protected Workbook workbook;
    /**
     * 数据格式化类
     */
    protected DataFormat dataformat;
    /**
     * 文件路径
     */
    protected String filePath;

    /**
     * 构造函数，不对外开放，可继承使用
     */
    protected ExcelSkelton() {
        workbook = new HSSFWorkbook();
        dataformat = workbook.createDataFormat();
    }

    /**
     * 构造函数，不对外开放，可继承使用
     * @param filePath 文件路径
     * @throws IOException
     */
    protected ExcelSkelton(String filePath) throws IOException {
        if (filePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(new FileInputStream(filePath));
        } else {
            workbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(filePath)));
        }
        dataformat = workbook.createDataFormat();
        this.filePath = filePath;
    }

    protected ExcelSkelton(String filePath, boolean isExcel2007) throws IOException {
        this(new FileInputStream(filePath), isExcel2007);
        this.filePath = filePath;
    }

    /**
     * 构造函数，不对外开放，可继承使用
     * @param in 输入流
     * @throws IOException
     */
    protected ExcelSkelton(InputStream in, boolean isExcel2007) throws IOException {
        if (isExcel2007) {
            workbook = new XSSFWorkbook(in);
        } else {
            workbook = new HSSFWorkbook(new POIFSFileSystem(in));
        }
        dataformat = workbook.createDataFormat();
    }

    /**
     * 由工作区序号返回工作区
     * @param sheetOrder 工作区序号
     * @param created 工作区不存在时是否创建
     * @return 工作区对象
     */
    public Sheet getSheet(int sheetOrder, boolean created) {
        Sheet sheet;
        if (sheetOrder < workbook.getNumberOfSheets()) {
            sheet = workbook.getSheetAt(sheetOrder);
        } else {
            sheet = created ? workbook.createSheet() : null;
        }
        return sheet;
    }

    /**
     * 得到一个工作区最后一条记录的序号
     * 
     * @return int 行号
     * @throws IOException
     */
    public int getLastRowNum() {
        return getLastRowNum(0);
    }

    /**
     * 得到一个工作区最后一条记录的序号
     * 
     * @param sheetOrder 工作区序号
     * @return int 行号
     * @throws IOException
     */
    public int getLastRowNum(int sheetOrder) {
        Sheet sheet = getSheet(sheetOrder, true);
        return sheet.getLastRowNum();
    }

    /**
     * 得到行对象
     * 
     * @param sheetOrder 工作区序号
     * @param rowNum 行号
     * @param created 该行不存在时是否创建
     * @return Row 行对象
     * @throws IOException
     */
    public Row getRow(int sheetOrder, int rowNum, boolean created) {
        Sheet sheet = getSheet(sheetOrder, created);
        Row row = null;
        if (sheet != null) {
            row = getRow(sheet, rowNum, created);
        }
        return row;
    }

    /**
     * 得到行对象
     * @param sheet 工作区对象
     * @param rowNum 行号
     * @param created 是否创建
     * @return 行对象
     */
    public Row getRow(Sheet sheet, int rowNum, boolean created) {
        Row row = sheet.getRow(rowNum);
        return (row == null && created) ? sheet.createRow(rowNum) : row;
    }

    /**
     * 得到单元格对象
     * @param sheet 工作区对象
     * @param rowNum 行号
     * @param cellNum 列号
     * @param created 是否创建
     * @return 单元格对象
     */
    public Cell getCell(Sheet sheet, int rowNum, int cellNum, boolean created) {
        Row row = getRow(sheet, rowNum, created);
        Cell cell = null;
        if (row != null) {
            cell = getCell(row, cellNum, created);
        }
        return cell;
    }

    /**
     * 得到单元格对象
     * @param sheetOrder 工作区序号
     * @param rowNum 行号
     * @param cellNum 列号
     * @param created 是否创建
     * @return 单元格对象
     */
    public Cell getCell(int sheetOrder, int rowNum, int cellNum, boolean created) {
        Row row = getRow(sheetOrder, rowNum, created);
        Cell cell = null;
        if (row != null) {
            cell = getCell(row, cellNum, created);
        }
        return cell;
    }

    /**
     * 得到单元格对象
     * @param row 行对象
     * @param cellNum 列号
     * @param created 是否创建
     * @return 单元格对象
     */
    public Cell getCell(Row row, int cellNum, boolean created) {
        Cell cell = row.getCell(cellNum);
        return (cell == null && created) ? row.createCell(cellNum) : cell;
    }

    /**
     * 设置单元格的格式
     * @param cell 单元格对象
     * @param format 格式
     */
    public void setCellFormat(Cell cell, String format) {
        // 设定单元格日期显示格式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(dataformat.getFormat(format));
        cell.setCellStyle(cellStyle);
    }

    /**
     * 取得单元格的计算函数
     * @param cell 单元格对象
     * @return 计算函数
     */
    protected String getFormula(Cell cell) {
        return (cell.getCellType() == Cell.CELL_TYPE_FORMULA) ? cell.getCellFormula() : "";
    }

    /**
     * 取得单元格的值，可根据单元格内容类型返回对应类型的对象，包含：Boolean,Byte(Error),Numeric,String
     * @param cell 单元格
     * @return 对象值
     */
    protected Object read(Cell cell) {
        Object content = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    content = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    content = "ERROR#" + String.valueOf(cell.getErrorCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case Cell.CELL_TYPE_NUMERIC:
                            content = getNumericValue(cell);
                            break;
                        case Cell.CELL_TYPE_STRING:
                            content = cell.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            content = cell.getBooleanCellValue();
                            break;
                        case Cell.CELL_TYPE_ERROR:
                        default:
                            content = cell.getStringCellValue();
                    }
                case Cell.CELL_TYPE_NUMERIC:
                    content = getNumericValue(cell);
                    break;
                case Cell.CELL_TYPE_BLANK:
                case Cell.CELL_TYPE_STRING:
                default:
                    content = cell.getStringCellValue();
                    break;
            }
        }
        return content;
    }

    private Object getNumericValue(Cell cell) {
        Object content;
        boolean isDate = false;
        if (DateUtil.isCellDateFormatted(cell)) {
            isDate = true;
        } else {
            if (cell.getCellStyle().getDataFormat() >= BuiltinFormats.FIRST_USER_DEFINED_FORMAT_INDEX) {
                String fmtString = cell.getCellStyle().getDataFormatString().toLowerCase();
                if (fmtString.indexOf('h') >= 0 || fmtString.indexOf('y') >= 0 || fmtString.indexOf('d') >= 0
                        || fmtString.indexOf('m') >= 0 || fmtString.indexOf('s') >= 0) {
                    isDate = true;
                }
            }
        }
        if (isDate) {
            content = cell.getDateCellValue();
        } else {
            content = cell.getNumericCellValue();
        }
        return content;
    }
}
