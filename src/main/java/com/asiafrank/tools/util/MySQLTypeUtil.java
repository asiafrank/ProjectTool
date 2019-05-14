package com.asiafrank.tools.util;

/**
 * @author user created at 5/15/2017.
 */
public final class MySQLTypeUtil {

    public static JavaType getJavaType(String columnTypeName) {
        String typeName;

        if (columnTypeName.contains("(")) {
            typeName = columnTypeName.substring(0, columnTypeName.indexOf("(")).trim();
        } else {
            typeName = columnTypeName;
        }

        if (Strings.indexOfIgnoreCase(typeName, "DECIMAL") != -1 || Strings.indexOfIgnoreCase(typeName, "DEC") != -1
                || Strings.indexOfIgnoreCase(typeName, "NUMERIC") != -1 || Strings.indexOfIgnoreCase(typeName, "FIXED") != -1) {
            return JavaType.DECIMAL;

        } else if (Strings.indexOfIgnoreCase(typeName, "TINYBLOB") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "TINYTEXT") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "TINYINT") != -1 || Strings.indexOfIgnoreCase(typeName, "TINY") != -1
                || Strings.indexOfIgnoreCase(typeName, "INT1") != -1) {
            return JavaType.TINYINT;

        } else if (Strings.indexOfIgnoreCase(typeName, "MEDIUMINT") != -1
                || Strings.indexOfIgnoreCase(typeName, "INT24") != -1 || Strings.indexOfIgnoreCase(typeName, "INT3") != -1
                || Strings.indexOfIgnoreCase(typeName, "MIDDLEINT") != -1) {
            return JavaType.INTEGER;

        } else if (Strings.indexOfIgnoreCase(typeName, "SMALLINT") != -1 || Strings.indexOfIgnoreCase(typeName, "INT2") != -1) {
            return JavaType.SMALLINT;

        } else if (Strings.indexOfIgnoreCase(typeName, "BIGINT") != -1 || Strings.indexOfIgnoreCase(typeName, "SERIAL") != -1
                || Strings.indexOfIgnoreCase(typeName, "INT8") != -1) {
            return JavaType.BIGINT;

        } else if (Strings.indexOfIgnoreCase(typeName, "POINT") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "INT") != -1 || Strings.indexOfIgnoreCase(typeName, "INTEGER") != -1
                || Strings.indexOfIgnoreCase(typeName, "INT4") != -1) {
            return JavaType.INTEGER;

        } else if (Strings.indexOfIgnoreCase(typeName, "DOUBLE") != -1 || Strings.indexOfIgnoreCase(typeName, "REAL") != -1
                || Strings.indexOfIgnoreCase(typeName, "FLOAT8") != -1) {
            return JavaType.DOUBLE;

        } else if (Strings.indexOfIgnoreCase(typeName, "FLOAT") != -1) {
            return JavaType.FLOAT;

        } else if (Strings.indexOfIgnoreCase(typeName, "NULL") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "TIMESTAMP") != -1) {
            return JavaType.TIMESTAMP;

        } else if (Strings.indexOfIgnoreCase(typeName, "DATETIME") != -1) {
            return JavaType.TIMESTAMP;

        } else if (Strings.indexOfIgnoreCase(typeName, "DATE") != -1) {
            return JavaType.DATE;

        } else if (Strings.indexOfIgnoreCase(typeName, "TIME") != -1) {
            return JavaType.TIME;

        } else if (Strings.indexOfIgnoreCase(typeName, "YEAR") != -1) {
            return JavaType.DATE;

        } else if (Strings.indexOfIgnoreCase(typeName, "LONGBLOB") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "LONGTEXT") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "MEDIUMBLOB") != -1 || Strings.indexOfIgnoreCase(typeName, "LONG VARBINARY") != -1) {
            return JavaType.LONGVARBINARY;

        } else if (Strings.indexOfIgnoreCase(typeName, "MEDIUMTEXT") != -1 || Strings.indexOfIgnoreCase(typeName, "LONG VARCHAR") != -1
                || Strings.indexOfIgnoreCase(typeName, "LONG") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "VARCHAR") != -1 || Strings.indexOfIgnoreCase(typeName, "NVARCHAR") != -1
                || Strings.indexOfIgnoreCase(typeName, "NATIONAL VARCHAR") != -1 || Strings.indexOfIgnoreCase(typeName, "CHARACTER VARYING") != -1) {
            return JavaType.VARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "VARBINARY") != -1) {
            return JavaType.VARBINARY;

        } else if (Strings.indexOfIgnoreCase(typeName, "BINARY") != -1 || Strings.indexOfIgnoreCase(typeName, "CHAR BYTE") != -1) {
            return JavaType.BINARY;

        } else if (Strings.indexOfIgnoreCase(typeName, "LINESTRING") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "STRING") != -1
                || Strings.indexOfIgnoreCase(typeName, "CHAR") != -1 || Strings.indexOfIgnoreCase(typeName, "NCHAR") != -1
                || Strings.indexOfIgnoreCase(typeName, "NATIONAL CHAR") != -1 || Strings.indexOfIgnoreCase(typeName, "CHARACTER") != -1) {
            return JavaType.CHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "BOOLEAN") != -1 || Strings.indexOfIgnoreCase(typeName, "BOOL") != -1) {
            return JavaType.BIT;

        } else if (Strings.indexOfIgnoreCase(typeName, "BIT") != -1) {
            return JavaType.BIT;

        } else if (Strings.indexOfIgnoreCase(typeName, "JSON") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "ENUM") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "SET") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "BLOB") != -1) {
            return JavaType.NULL;

        } else if (Strings.indexOfIgnoreCase(typeName, "TEXT") != -1) {
            return JavaType.LONGVARCHAR;

        } else if (Strings.indexOfIgnoreCase(typeName, "GEOMETRY") != -1 // also covers "GEOMETRYCOLLECTION"
                || Strings.indexOfIgnoreCase(typeName, "POINT") != -1 // also covers "MULTIPOINT"
                || Strings.indexOfIgnoreCase(typeName, "POLYGON") != -1 // also covers "MULTIPOLYGON"
        ) {
            return JavaType.NULL;
        }

        return JavaType.NULL;
    }
}
