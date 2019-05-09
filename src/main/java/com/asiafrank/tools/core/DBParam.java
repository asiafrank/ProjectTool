package com.asiafrank.tools.core;

import com.asiafrank.tools.util.DB;

/**
 * @author asiafrank created at 1/5/2017.
 */
@Deprecated
public class DBParam {

    private final DB db;

    private final String database;

    private final String username;

    private final String password;

    private final String schema;

    private final String tablePrefix;

    private final String[] tableNames;

    private final String driver;

    private final String url;

    public DBParam(
            String database,
            String username,
            String password,
            DB db,
            String schema,
            String tablePrefix,
            String[] tableNames)
    {
        this.db = db;
        this.database = database;
        this.username = username;
        this.password = password;

        this.schema = nullOrEmpty(schema) ? "" : schema;
        this.tablePrefix = nullOrEmpty(tablePrefix) ? "" : tablePrefix;
        if (tableNames == null || tableNames.length == 0) {
            throw new IllegalArgumentException("tableNames should not be null");
        }
        this.tableNames = tableNames;

        if (this.db == DB.MYSQL) {
            this.driver = "com.mysql.cj.jdbc.Driver";
            this.url = "jdbc:mysql://localhost:3306/" + database + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=UTC";
        } else {
            this.driver = "org.postgresql.Driver";
            this.url = "jdbc:postgresql://localhost/" + database;
        }
    }

    public DB getDb() {
        return db;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getSchema() {
        return schema;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
