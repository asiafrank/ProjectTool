package com.asiafrank.tools.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 * @author user created at 5/5/2017.
 */
public class CloseUtil {
    private static final Logger log = Logger.getLogger("CloseUtil");

    /**
     * 关闭连接
     *
     * @param conn
     * @throws SQLException
     */
    public static void close(Connection conn) throws SQLException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Can not close connection.", e);
        }
    }

    /**
     * 关闭查询
     *
     * @param statement
     * @throws SQLException
     */
    public static void close(Statement statement) throws SQLException {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Can not close statement.", e);
        }
    }

    /**
     * 关闭结果
     *
     * @param resultSet
     * @throws SQLException
     */
    public static void close(ResultSet resultSet) throws SQLException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Can not close resultSet.", e);
        }
    }
}
