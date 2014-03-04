package com.ehr.framework.worker.workhandler;

/**
 * 分页信息扩展实体
 * @author zoe
 */
public interface PageExtendedEntity {

    public int getPageIndex();

    public void setPageIndex(final String pageIndex);

    public int getPageNum();

    public void setPageNum(final String pageNum);

    public int getPageRows();

    public void setPageRows(final String pageRows);

    public int getTotalRows();

    public void setTotalRows(final String totalRows);

    /**
     * 判断目标页是否存在
     * @param count
     * @return boolean
     */
    public boolean isPageDataExist(final int count);

    /**
     * 获取目标页记录的起始记录索引
     * @param totalRowsInt
     * @return int
     */
    public int getPageDataStartIndex();
}
