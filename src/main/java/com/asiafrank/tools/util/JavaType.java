package com.asiafrank.tools.util;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * @author user created at 5/15/2017.
 */
public final class JavaType {
    public static final JavaType BIT = new JavaType(JDBCType.BIT, Boolean.class);

    public static final JavaType TINYINT = new JavaType(JDBCType.TINYINT, Byte.class);
    public static final JavaType SMALLINT = new JavaType(JDBCType.SMALLINT, Short.class);
    public static final JavaType INTEGER = new JavaType(JDBCType.INTEGER, Integer.class);
    public static final JavaType BIGINT = new JavaType(JDBCType.BIGINT, Long.class);

    public static final JavaType REAL = new JavaType(JDBCType.REAL, Float.class);
    public static final JavaType FLOAT = new JavaType(JDBCType.FLOAT, Double.class);
    public static final JavaType DOUBLE = new JavaType(JDBCType.DOUBLE, Double.class);
    public static final JavaType NUMERIC = new JavaType(JDBCType.NUMERIC, BigDecimal.class);
    public static final JavaType DECIMAL = new JavaType(JDBCType.DECIMAL, BigDecimal.class);

    public static final JavaType CHAR = new JavaType(JDBCType.CHAR, String.class);
    public static final JavaType VARCHAR = new JavaType(JDBCType.VARCHAR, String.class);
    public static final JavaType LONGVARCHAR = new JavaType(JDBCType.LONGVARCHAR, String.class);

    public static final JavaType BINARY = new JavaType(JDBCType.BINARY, Byte[].class);
    public static final JavaType VARBINARY = new JavaType(JDBCType.VARBINARY, Byte[].class);
    public static final JavaType LONGVARBINARY = new JavaType(JDBCType.LONGVARBINARY, Byte[].class);

    public static final JavaType DATE = new JavaType(JDBCType.DATE, LocalDate.class);
    public static final JavaType TIME = new JavaType(JDBCType.TIME, LocalTime.class);
    public static final JavaType TIME_WITH_TIMEZONE = new JavaType(JDBCType.TIME_WITH_TIMEZONE, LocalTime.class);
    public static final JavaType TIMESTAMP = new JavaType(JDBCType.TIMESTAMP, LocalDateTime.class);
    public static final JavaType TIMESTAMP_WITH_TIMEZONE = new JavaType(JDBCType.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class);

    public static final JavaType NULL = new JavaType(JDBCType.NULL, Object.class);

    private final JDBCType jdbcType;
    private final Class<?> javaClass;
    private final boolean isArray;

    public JavaType(JDBCType jdbcType, Class<?> javaClass) {
        this(jdbcType, javaClass, false);
    }

    JavaType(JDBCType jdbcType, Class<?> javaClass, boolean isArray) {
        this.jdbcType = jdbcType;
        this.javaClass = javaClass;
        this.isArray = isArray;
    }

    public JDBCType getJdbcType() {
        return jdbcType;
    }

    public String getJdbcTypeName() {
        return getJdbcType().getName();
    }

    public Class<?> getJavaClass() {
        return javaClass;
    }

    public String getJavaTypeName() {
        String n = getJavaClass().getCanonicalName();
        return (!isArray)? n : n + "[]";
    }

    public String getJavaSimpleName() {
        String n = getJavaClass().getSimpleName();
        return (!isArray)? n : n + "[]";
    }
}
