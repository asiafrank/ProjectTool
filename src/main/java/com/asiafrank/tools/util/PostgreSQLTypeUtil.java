package com.asiafrank.tools.util;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author user created at 5/15/2017.
 */
public class PostgreSQLTypeUtil {

    private static final HashMap<String, JavaType> mapping = new HashMap<>();

    // not support array type
    static {
        mapping.put("int2", JavaType.SMALLINT);
        mapping.put("smallint", JavaType.SMALLINT);
        mapping.put("int", JavaType.INTEGER);
        mapping.put("int4", JavaType.INTEGER);
        mapping.put("integer", JavaType.INTEGER);
        mapping.put("int8", JavaType.BIGINT);
        mapping.put("oid", JavaType.BIGINT);
        mapping.put("money", JavaType.DOUBLE);
        mapping.put("numeric", JavaType.NUMERIC);
        mapping.put("decimal", JavaType.NUMERIC);
        mapping.put("float4", JavaType.REAL);
        mapping.put("float", JavaType.DOUBLE);
        mapping.put("float8", JavaType.DOUBLE);
        mapping.put("char", JavaType.CHAR);
        mapping.put("bpchar", JavaType.CHAR);
        mapping.put("varchar", JavaType.VARCHAR);
        mapping.put("text", JavaType.VARCHAR);
        mapping.put("bytea", JavaType.BINARY);
        mapping.put("bool", JavaType.BIT);
        mapping.put("boolean", JavaType.BIT);
        mapping.put("bit", JavaType.BIT);
        mapping.put("date", JavaType.DATE);
        mapping.put("time", JavaType.TIME);
        mapping.put("timetz", JavaType.TIME_WITH_TIMEZONE);
        mapping.put("timestamp", JavaType.TIMESTAMP);
        mapping.put("timestamptz", JavaType.TIMESTAMP_WITH_TIMEZONE);
    }

    public static JavaType getJavaType(String columnTypeName) {
        String typeName;
        boolean isArray = false;
        if (columnTypeName.endsWith("[]")) {
            isArray = true;
            int i = columnTypeName.indexOf("[");
            typeName = columnTypeName.substring(0, i);
        } else {
            typeName = columnTypeName;
        }

        JavaType t = mapping.get(typeName);

        if (Objects.isNull(t)) {
            t = JavaType.NULL;
        }

        if (isArray) {
            t = new JavaType(t.getJdbcType(), t.getJavaClass(), true);
        }

        return t;
    }
}
