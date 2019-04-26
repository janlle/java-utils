package com.leone.util.jdbc.callback;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * <p>
 *
 * @author leone
 **/
@FunctionalInterface
public interface DbCallBack {

    /**
     * 数据库元数据操作
     *
     * @param db
     * @throws SQLException
     */
    void call(DatabaseMetaData db) throws SQLException;

}
