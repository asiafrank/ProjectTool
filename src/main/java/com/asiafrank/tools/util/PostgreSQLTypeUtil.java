package com.asiafrank.tools.util;

/**
 * @author user created at 5/15/2017.
 */
public class PostgreSQLTypeUtil {

    public static String getJavaTypeName(String columnName) {
        JavaType t = getJavaType(columnName);
        return t.getJavaClass().getCanonicalName();
    }

    public static JavaType getJavaType(String columnName) {
        // ref: TypeInfoCache
        return JavaType.NULL;
    }
}
