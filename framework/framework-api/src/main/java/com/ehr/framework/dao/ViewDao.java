package com.ehr.framework.dao;

import java.util.List;

/**
 * dynamic entity dao
 * @author zoe
 */
public interface ViewDao<V extends View> {

    /**
     * 多条件动态查询
     * @param conditionList 条件集合
     * @return 
     */
    public List<V> inquireByCondition(final List<Condition> conditionList);

    /**
     * 多条件动态查询，带排序条件
     * @param conditionList
     * @param orderList
     * @return 
     */
    public List<V> inquireByCondition(final List<Condition> conditionList, final List<Order> orderList);

    /**
     * 多条件动态统计
     */
    public int countByCondition(final List<Condition> conditionList);

    /**
     * 多条件动态分页查询
     * @param conditionList 条件集合
     * @param startIndex 起始行数，第一行为0
     * @param rows 起始行开始取多少行记录
     * @return 
     */
    public List<V> inquirePageByCondition(final List<Condition> conditionList, final int startIndex, final int rows);

    /**
     * 多条件动态分页查询，带排序条件
     * @param conditionList 条件集合
     * @param orderList 
     * @param startIndex 起始行数，第一行为0
     * @param rows 起始行开始取多少行记录
     * @return 
     */
    public List<V> inquirePageByCondition(final List<Condition> conditionList, final List<Order> orderList, final int startIndex, final int rows);
}
