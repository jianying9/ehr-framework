package com.ehr.framework.worker.workhandler;

import com.ehr.framework.entity.ExtendedEntityConfig;
import com.ehr.framework.entity.field.FieldConfig;
import com.ehr.framework.entity.field.type.FieldTypeEnum;

/**
 * 分页信息扩展实体
 * @author zoe
 */
@ExtendedEntityConfig(extendedEntityName = "PageExtended")
public final class PageExtendedEntityImpl implements PageExtendedEntity{

    @FieldConfig(type = FieldTypeEnum.INT_SIGNED, fieldDesc = "分页查询:页码索引")
    private int pageIndex;
    @FieldConfig(type = FieldTypeEnum.INT_SIGNED, fieldDesc = "分页查询:每页显示行数")
    private int pageRows;
    @FieldConfig(type = FieldTypeEnum.INT_SIGNED, fieldDesc = "分页查询:查询结果总记录数")
    private int totalRows;
    @FieldConfig(type = FieldTypeEnum.INT_SIGNED, fieldDesc = "分页查询:查询结果总页数")
    private int pageNum;

    PageExtendedEntityImpl() {
        this.pageNum = 0;
        this.totalRows = 0;
    }

    @Override
    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void setPageIndex(final String pageIndex) {
        if (pageIndex.equals("0")) {
            this.pageIndex = 1;
        } else {
            this.pageIndex = Integer.parseInt(pageIndex);
        }
    }

    @Override
    public int getPageNum() {
        return pageNum;
    }

    @Override
    public void setPageNum(final String pageNum) {
        this.pageNum = Integer.parseInt(pageNum);
    }

    @Override
    public int getPageRows() {
        return pageRows;
    }

    @Override
    public void setPageRows(final String pageRows) {
        if (pageRows.equals("0")) {
            this.pageRows = 15;
        } else {
            this.pageRows = Integer.parseInt(pageRows);
        }
    }

    @Override
    public int getTotalRows() {
        return totalRows;
    }

    @Override
    public void setTotalRows(final String totalRows) {
        this.totalRows = Integer.parseInt(totalRows);
    }

    /**
     * 判断目标页是否存在
     * @param count
     * @return boolean
     */
    @Override
    public boolean isPageDataExist(final int count) {
        boolean isExist = false;//return
        //判断pageIndex是否存在
        if ((this.pageIndex - 1) * this.pageRows < count) {
            this.totalRows = count;
            //计算总页数pageNum
            if (count % this.pageRows == 0) {
                this.pageNum = count / this.pageRows;
            } else {
                this.pageNum = count / this.pageRows + 1;
            }
            isExist = true;
        }
        return isExist;
    }

    /**
     * 获取目标页记录的起始记录索引
     * @param totalRowsInt
     * @return int
     */
    @Override
    public int getPageDataStartIndex() {
        int startIndex = -1;//return
        //判断pageIndex是否存在
        if ((this.pageIndex - 1) * this.pageRows < this.totalRows) {
            startIndex = (this.pageIndex - 1) * this.pageRows;
        }
        return startIndex;
    }
}
