package com.ehr.framework.datasource;

import com.ehr.framework.logger.LogFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 * 数据源创建类，默认只能连接mysql数据库
 * @author zoe
 */
public final class DataSourceBuilder {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private DataSourceTypeEnum dataSourceTypeEnum;
    private String url = "";
    private String userName = "";
    private String password = "";
    private String jndiName = "";

    public DataSourceBuilder(DataSourceTypeEnum dataSourceTypeEnum) {
        if (dataSourceTypeEnum != null) {
            this.dataSourceTypeEnum = dataSourceTypeEnum;
        } else {
            throw new RuntimeException("There was an error building DataSource : dataSourceTypeEnum is null");
        }
    }

    public DataSourceTypeEnum getDataSourceTypeEnum() {
        return dataSourceTypeEnum;
    }

    public String getJndiName() {
        return jndiName;
    }

    public DataSourceBuilder setJndiName(String jndiName) {
        this.jndiName = jndiName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DataSourceBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DataSourceBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public DataSourceBuilder setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public DataSource build() {
        DataSource dataSource = null;
        switch (this.dataSourceTypeEnum) {
            case GENERAL:
                dataSource = this.generalBuild();
                break;
            case JNDI:
                dataSource = this.jndiBuild();
                break;
        }
        return dataSource;
    }

    /**
     *  根据url、userName、password获取DataSource
     * @return 
     */
    private DataSource generalBuild() {
        return new GeneralDataSource().setUrl(this.url).setUserName(this.userName).setPassword(this.password).build();
    }

    /**
     * 根据jndi获取DataSource
     * @return 
     */
    private DataSource jndiBuild() {
        DataSource dataSource = null;
        //数据属性检测
        if (this.jndiName == null || this.jndiName.isEmpty()) {
            throw new RuntimeException("There was an error building jndidataSource. Cause: jndiName is null or empty");
        }
        try {
            InitialContext initCtx = new InitialContext();
            dataSource = (DataSource) initCtx.lookup(this.jndiName);
        } catch (NamingException e) {
            logger.error("There was an error building jndidataSource. Cause: a NamingException occurd", e);
            throw new RuntimeException("There was an error building jndidataSource. Cause: ".concat(e.getMessage()));
        }
        return dataSource;
    }
}
