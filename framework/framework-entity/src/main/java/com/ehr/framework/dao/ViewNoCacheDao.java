package com.ehr.framework.dao;

import com.ehr.framework.jdbc.SqlBuilder;
import com.ehr.framework.jdbc.SqlExecuter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * dynamic entity dao
 * @author zoe
 */
public final class ViewNoCacheDao<V extends View> extends AbstractDao<V> implements ViewDao<V> {

    private String countSqlModel;

    ViewNoCacheDao() {
    }

    void setCountSqlModel(String countSqlModel) {
        this.countSqlModel = countSqlModel;
    }

    /**
     * 多条件动态查询
     * @param conditionList 条件集合
     * @return 
     */
    @Override
    public List<V> inquireByCondition(final List<Condition> conditionList) {
        List<V> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<V>(0);
        } else {
            String inquireSql = SqlBuilder.conditionSqlBuild(this.inquireSqlModel, conditionList);
            String[] values = new String[conditionList.size()];
            for (int index = 0; index < conditionList.size(); index++) {
                values[index] = conditionList.get(index).getColumnValue();
            }
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件动态查询，带排序条件
     * @param conditionList
     * @param orderList
     * @return 
     */
    @Override
    public List<V> inquireByCondition(final List<Condition> conditionList, final List<Order> orderList) {
        List<V> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<V>(0);
        } else {
            String inquireSql = SqlBuilder.conditionSqlBuild(this.inquireSqlModel, conditionList, orderList);
            String[] values = new String[conditionList.size()];
            for (int index = 0; index < conditionList.size(); index++) {
                values[index] = conditionList.get(index).getColumnValue();
            }
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件统计
     * @param conditionList
     * @return 
     */
    @Override
    public int countByCondition(List<Condition> conditionList) {
        int count = 0;
        if (!conditionList.isEmpty()) {
            String countSql = SqlBuilder.countViewSqlBuild(this.countSqlModel, conditionList);
            List<String> valueList = new ArrayList<String>(conditionList.size());
            for (Condition condition : conditionList) {
                if (!condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    valueList.add(condition.getColumnValue());
                }
            }
            String[] values = valueList.toArray(new String[valueList.size()]);
            count = SqlExecuter.count(this.dataSource, countSql, values);
        }
        return count;
    }

    /**
     * 多条件动态分页查询
     * @param conditionList 条件集合
     * @param startIndex 起始行数，第一行为0
     * @param rows 起始行开始取多少行记录
     * @return 
     */
    @Override
    public List<V> inquirePageByCondition(List<Condition> conditionList, int startIndex, int rows) {
        List<V> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<V>(0);
        } else {
            String inquireSql = SqlBuilder.pageConditionSqlBuild(this.inquireSqlModel, conditionList, startIndex, rows);
            List<String> valueList = new ArrayList<String>(conditionList.size());
            for (Condition condition : conditionList) {
                if (!condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    valueList.add(condition.getColumnValue());
                }
            }
            String[] values = valueList.toArray(new String[valueList.size()]);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }

    /**
     * 多条件动态分页查询，带排序条件
     * @param conditionList 条件集合
     * @param orderList 
     * @param startIndex 起始行数，第一行为0
     * @param rows 起始行开始取多少行记录
     * @return 
     */
    @Override
    public List<V> inquirePageByCondition(List<Condition> conditionList, List<Order> orderList, int startIndex, int rows) {
        List<V> tList;
        if (conditionList.isEmpty()) {
            tList = new ArrayList<V>(0);
        } else {
            String inquireSql = SqlBuilder.pageConditionSqlBuild(this.inquireSqlModel, conditionList, orderList, startIndex, rows);
            List<String> valueList = new ArrayList<String>(conditionList.size());
            for (Condition condition : conditionList) {
                if (!condition.getKeyWord().equals(ConditionTypeEnum.IN.getKeyWord())) {
                    valueList.add(condition.getColumnValue());
                }
            }
            String[] values = valueList.toArray(new String[valueList.size()]);
            List<Map<String, String>> resultMapList = SqlExecuter.inquireList(this.dataSource, inquireSql, values);
            tList = this.parseMapList(resultMapList);
        }
        return tList;
    }
}
