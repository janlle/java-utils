package com.leone.util.jdbc.conn;

import java.sql.*;

/**
 * <p>
 *
 * @author leone
 * @since 2019-06-05
 **/
public class SqlServerConnUtil {

    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String URL = "jdbc:sqlserver://39.108.125.41:1433;databasename=master";
    private static final String USERNAME = "SA";
    private static final String PASSWORD = "Rl)^@)15";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeAll(Connection cn, Statement st, ResultSet rs) {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cn = null;
            }
        }

        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                st = null;
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                rs = null;
            }
        }

    }

    public static void main(String[] args) {
        System.out.println(getConnection());
    }

}
