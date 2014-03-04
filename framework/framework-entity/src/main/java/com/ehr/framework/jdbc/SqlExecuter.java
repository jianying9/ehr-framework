package com.ehr.framework.jdbc;

import com.ehr.framework.logger.LogFactory;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 * sql执行者
 * @author zoe
 */
public final class SqlExecuter {

    private SqlExecuter() {
    }
    private static final Logger logger = LogFactory.getFrameworkLogger();

    /**
     * 根据条件查询主键集合
     * @param dataSource 数据源
     * @param inquireKeysSql 主键查询sql
     * @param value 条件值
     * @return 
     */
    public static List<Long> inquireKeys(final DataSource dataSource, final String inquireKeysSql, final String value) {
        logger.debug("inquireKeysSql:{}", inquireKeysSql);
        logger.debug("values:{}", value);
        Connection conn = null;
        List<Long> keyValueList = new ArrayList<Long>(0);
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireKeysSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setString(1, value);
            ResultSet rs = stat.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount > 0) {
                long keyValue;
                keyValueList = new ArrayList<Long>(rowCount);
                rs.beforeFirst();
                while (rs.next()) {
                    keyValue = rs.getLong(1);
                    keyValueList.add(keyValue);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireKeys", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireKeys.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return keyValueList;
    }

    public static List<Long> inquireKeys(final DataSource dataSource, final String inquireKeysSql, final String[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("inquireKeysSql:{}", inquireKeysSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        List<Long> keyValueList = new ArrayList<Long>(0);
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireKeysSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            ResultSet rs = stat.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount > 0) {
                long keyValue;
                keyValueList = new ArrayList<Long>(rowCount);
                rs.beforeFirst();
                while (rs.next()) {
                    keyValue = rs.getLong(1);
                    keyValueList.add(keyValue);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireKeys", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireKeys.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return keyValueList;
    }

    /**
     * 查询总记录数
     * @param dataSource
     * @param countSql
     * @param values
     * @return 
     */
    public static int count(final DataSource dataSource, final String countSql, final String[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("countSql:{}", countSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        int count = 0;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(countSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("countSql", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing countSql.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return count;
    }

    /**
     * 主键查询
     * @param dataSource 数据源
     * @param inquireByKeySql 待执行的sql语句
     * @param value 键值
     * @return 
     */
    public static Map<String, String> inquireByKey(final DataSource dataSource, final String inquireByKeySql, final long value) {
        logger.debug("inquireByKeySql:{}", inquireByKeySql);
        logger.debug("values:{}", value);
        Connection conn = null;
        Map<String, String> resultMap = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireByKeySql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setLong(1, value);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                resultMap = new HashMap<String, String>(columnCount, 1);
                for (int index = 1; index <= columnCount; index++) {
                    resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireByKey", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireList.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return resultMap;
    }

    /**
     * 普通SQL查询
     * @param dataSource
     * @param inquireSql
     * @return 
     */
    public static List<Map<String, String>> inquire(final DataSource dataSource, final String inquireSql) {
        logger.debug("inquireSql:{}", inquireSql);
        Connection conn = null;
        List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>(0);
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stat.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount > 0) {
                Map<String, String> resultMap;
                resultMapList = new ArrayList<Map<String, String>>(rowCount);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                rs.beforeFirst();
                while (rs.next()) {
                    resultMap = new HashMap<String, String>(columnCount, 1);
                    for (int index = 1; index <= columnCount; index++) {
                        resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                    }
                    resultMapList.add(resultMap);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireSql", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireSql.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return resultMapList;
    }

    /**
     * 单条件查询
     * @param dataSource 数据源
     * @param inquireSql 查询语句
     * @param value 条件值
     * @return 
     */
    public static List<Map<String, String>> inquireList(final DataSource dataSource, final String inquireSql, final String value) {
        logger.debug("inquireSql:{}", inquireSql);
        logger.debug("values:{}", value);
        Connection conn = null;
        List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>(0);
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setString(1, value);
            ResultSet rs = stat.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount > 0) {
                Map<String, String> resultMap;
                resultMapList = new ArrayList<Map<String, String>>(rowCount);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                rs.beforeFirst();
                while (rs.next()) {
                    resultMap = new HashMap<String, String>(columnCount, 1);
                    for (int index = 1; index <= columnCount; index++) {
                        resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                    }
                    resultMapList.add(resultMap);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireList", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireList.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return resultMapList;
    }

    /**
     * 多条件查询
     * @param dataSource 数据源
     * @param inquireSql 查询语句
     * @param value 条件值
     * @return 
     */
    public static List<Map<String, String>> inquireList(final DataSource dataSource, final String inquireSql, final String[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("inquireSql:{}", inquireSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        List<Map<String, String>> resultMapList = new ArrayList<Map<String, String>>(0);
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(inquireSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            ResultSet rs = stat.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount > 0) {
                Map<String, String> resultMap;
                resultMapList = new ArrayList<Map<String, String>>(rowCount);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                rs.beforeFirst();
                while (rs.next()) {
                    resultMap = new HashMap<String, String>(columnCount, 1);
                    for (int index = 1; index <= columnCount; index++) {
                        resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                    }
                    resultMapList.add(resultMap);
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("inquireList", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing inquireList.Cause:").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return resultMapList;
    }

    /**
     * 插入
     * @param dataSource 数据源
     * @param insertSql 待执行sql
     * @param values 插入值
     */
    public static void insert(final DataSource dataSource, final String insertSql, final String[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertSql:{}", insertSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(insertSql);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            stat.executeUpdate();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("insert", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing insert.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 批量插入
     * @param dataSource
     * @param batchInsertSql
     * @param valueArrays 
     */
    public static void batchInsert(final DataSource dataSource, final String batchInsertSql, final String[][] valueArrays) {
        if (logger.isDebugEnabled()) {
            logger.debug("batchInsertSql:{}", batchInsertSql);
            logger.debug("valueArrays:{}", SqlBuilder.valuesBuild(valueArrays));
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(batchInsertSql);
            int columnIndex;
            String[] values;
            for (int rowIndex = 0; rowIndex < valueArrays.length; rowIndex++) {
                values = valueArrays[rowIndex];
                for (int index = 0; index < values.length; index++) {
                    columnIndex = index + 1;
                    stat.setString(columnIndex, values[index]);
                }
                stat.addBatch();
            }
            stat.executeBatch();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("batch insert", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing batch insert.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 插入并查询该插入行
     * @param dataSource 数据源
     * @param insertSql 插入SQL
     * @param values 插入值
     * @param inquireByKeySql 查询SQL
     * @return 
     */
    public static Map<String, String> insertAndInquireByKey(final DataSource dataSource, final String insertSql, final String[] values, final String inquireByKeySql) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertSql:{}", insertSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        Map<String, String> resultMap = null;
        try {
            conn = dataSource.getConnection();
            //插入
            PreparedStatement stat = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            stat.executeUpdate();
            //获取key值
            ResultSet rs = stat.getGeneratedKeys();
            String keyValue = "-1";
            if (rs.next()) {
                keyValue = rs.getString(1);
            }
            rs.close();
            stat.close();
            //查询插入值
            logger.debug("inquireByKeySql:{}", inquireByKeySql);
            logger.debug("value:{}", keyValue);
            if (!keyValue.equals("-1")) {
                stat = conn.prepareStatement(inquireByKeySql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                stat.setString(1, keyValue);
                rs = stat.executeQuery();
                if (rs.next()) {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    resultMap = new HashMap<String, String>(columnCount, 1);
                    for (int index = 1; index <= columnCount; index++) {
                        resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                    }
                }
                rs.close();
                stat.close();
            }
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("insertAndInquire", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing insertAndInquire.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        if (resultMap == null) {
            throw new RuntimeException("There was an error inserting data.Cause: UNKNOW");
        }
        return resultMap;
    }

    /**
     * 删除
     * @param dataSource 数据源
     * @param deleteSql 删除SQL
     * @param value 值
     */
    public static void delete(final DataSource dataSource, final String deleteSql, final long value) {
        logger.debug("deleteSql:{}", deleteSql);
        logger.debug("value:{}", value);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(deleteSql);
            stat.setLong(1, value);
            stat.executeUpdate();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("delete", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing delete.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 删除
     * @param dataSource 数据源
     * @param deleteSql 删除SQL
     * @param value 值
     */
    @Deprecated
    public static void deleteAll(final DataSource dataSource, final String deleteSql) {
        logger.debug("deleteSql:{}", deleteSql);
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(deleteSql);
            stat.executeUpdate();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("delete", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing delete.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 批量删除
     * @param dataSource
     * @param deleteSql
     * @param value 
     */
    public static void batchDelete(final DataSource dataSource, final String batchDeleteSql, final Collection<Long> keyValues) {
        if (logger.isDebugEnabled()) {
            logger.debug("insertSql:{}", batchDeleteSql);
            logger.debug("value:{}", SqlBuilder.collectionBuild(keyValues));
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(batchDeleteSql);
            for (Long keyValue : keyValues) {
                stat.setLong(1, keyValue);
                stat.addBatch();
            }
            stat.executeBatch();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("batch delete", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing batch delete.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 更新
     * @param dataSource 数据源
     * @param updateSql 更新SQL
     * @param values 更新值
     */
    public static void update(final DataSource dataSource, final String updateSql, final String[] values) {
        if (logger.isDebugEnabled()) {
            logger.debug("updateSql:{}", updateSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(updateSql);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            stat.executeUpdate();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("update", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing update.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 批量更新
     * @param dataSource
     * @param batchUpdateSql
     * @param valueArrays 
     */
    public static void batchUpdate(final DataSource dataSource, final String batchUpdateSql, final String[][] valueArrays) {
        if (logger.isDebugEnabled()) {
            logger.debug("batchUpdateSql:{}", batchUpdateSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(valueArrays));
        }
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(batchUpdateSql);
            int columnIndex;
            String[] values;
            for (int rowIndex = 0; rowIndex < valueArrays.length; rowIndex++) {
                values = valueArrays[rowIndex];
                for (int index = 0; index < values.length; index++) {
                    columnIndex = index + 1;
                    stat.setString(columnIndex, values[index]);
                }
                stat.addBatch();
            }
            stat.executeBatch();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("batch update", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing batch update.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    /**
     * 更新并查询更新后值,约定values中的最后一个值为keyValue
     * @param dataSource 数据源
     * @param updateSql 更新语句
     * @param values 更新值
     * @param inquireByKeySql 查询SQL
     * @return 
     */
    public static Map<String, String> updateAndInquireByKey(final DataSource dataSource, final String updateSql, final String[] values, final String inquireByKeySql) {
        if (logger.isDebugEnabled()) {
            logger.debug("updateSql:{}", updateSql);
            logger.debug("values:{}", SqlBuilder.valuesBuild(values));
        }
        Connection conn = null;
        Map<String, String> resultMap = null;
        try {
            conn = dataSource.getConnection();
            //更新
            PreparedStatement stat = conn.prepareStatement(updateSql);
            int columnIndex;
            for (int index = 0; index < values.length; index++) {
                columnIndex = index + 1;
                stat.setString(columnIndex, values[index]);
            }
            stat.executeUpdate();
            stat.close();
            //查询
            String keyValue = values[values.length - 1];
            logger.debug("inquireByKeySql:{}", inquireByKeySql);
            logger.debug("value:{}", keyValue);
            stat = conn.prepareStatement(inquireByKeySql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            stat.setString(1, keyValue);
            ResultSet rs = stat.executeQuery();
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                resultMap = new HashMap<String, String>(columnCount, 1);
                for (int index = 1; index <= columnCount; index++) {
                    resultMap.put(rsmd.getColumnLabel(index), rs.getString(index));
                }
            }
            rs.close();
            stat.close();
        } catch (SQLException e) {
            Throwable throwable = null;
            for (Throwable t : e) {
                logger.error("updateAndInquireByKey", t);
                throwable = t;
                break;
            }
            StringBuilder mesBuilder = new StringBuilder(200);
            mesBuilder.append("There was an error executing update.Cause: ").append(throwable.getMessage());
            throw new RuntimeException(mesBuilder.toString());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return resultMap;
    }
}
