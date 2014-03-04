package com.ehr.framework.datasource;

import com.ehr.framework.logger.LogFactory;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import javax.sql.DataSource;
import org.slf4j.Logger;

/**
 *
 * @author zoe
 */
public final class GeneralDataSource implements DataSource {

    private final Logger logger = LogFactory.getFrameworkLogger();
    private final String driver = "com.mysql.jdbc.Driver";
    private String url = "";
    private String userName = "";
    private String password = "";

    GeneralDataSource() {
    }

    GeneralDataSource setPassword(String password) {
        this.password = password;
        return this;
    }

    GeneralDataSource setUrl(String url) {
        this.url = url;
        return this;
    }

    GeneralDataSource setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    GeneralDataSource build() {
        if (this.url == null || this.url.isEmpty()) {
            throw new RuntimeException("There was an error building generaldataSource. Cause: url is null or empty");
        }
        if (this.userName == null || this.userName.isEmpty()) {
            throw new RuntimeException("There was an error building generaldataSource. Cause: userName is null or empty");
        }
        if (this.password == null || this.password.isEmpty()) {
            throw new RuntimeException("There was an error building generaldataSource. Cause: password is null or empty");
        }
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Class<?> driverType = Class.forName(this.driver, true, classLoader);
            DriverManager.registerDriver((Driver) driverType.newInstance());
        } catch (Exception e) {
            logger.error("There was an error building generaldataSource. Cause: a Exception occurd", e);
            throw new RuntimeException("There was an error building generaldataSource. Cause: ".concat(e.getMessage()));
        }
        return this;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(this.url, this.userName, this.password);
        connection.setAutoCommit(true);
        return connection;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        logger.warn("method getConnection(String username, String password) is invalid!");
        return this.getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
