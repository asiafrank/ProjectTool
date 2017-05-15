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
public enum JavaType {
    BIT(JDBCType.BIT, Boolean.class),

    TINYINT(JDBCType.TINYINT, Byte.class),
    SMALLINT(JDBCType.SMALLINT, Short.class),
    INTEGER(JDBCType.INTEGER, Integer.class),
    BIGINT(JDBCType.BIGINT, Long.class),

    REAL(JDBCType.REAL, Float.class),
    FLOAT(JDBCType.FLOAT, Double.class),
    DOUBLE(JDBCType.DOUBLE, Double.class),
    NUMERIC(JDBCType.NUMERIC, BigDecimal.class),
    DECIMAL(JDBCType.DECIMAL, BigDecimal.class),

    CHAR(JDBCType.CHAR, String.class),
    VARCHAR(JDBCType.VARCHAR, String.class),
    LONGVARCHAR(JDBCType.LONGVARCHAR, String.class),

    BINARY(JDBCType.BINARY, Byte[].class),
    VARBINARY(JDBCType.VARBINARY, Byte[].class),
    LONGVARBINARY(JDBCType.LONGVARBINARY, Byte[].class),

    DATE(JDBCType.DATE, LocalDate.class),
    TIME(JDBCType.TIME, LocalTime.class),
    TIME_WITH_TIMEZONE(JDBCType.TIME_WITH_TIMEZONE, LocalTime.class),
    TIMESTAMP(JDBCType.TIMESTAMP, LocalDateTime.class),
    TIMESTAMP_WITH_TIMEZONE(JDBCType.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class),

    NULL(JDBCType.NULL, Object.class)
    ;
    private final JDBCType jdbcType;
    private final Class<?> javaClass;

    JavaType(JDBCType jdbcType, Class<?> javaClass) {
        this.jdbcType = jdbcType;
        this.javaClass = javaClass;
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
        return getJavaClass().getCanonicalName();
    }
}
